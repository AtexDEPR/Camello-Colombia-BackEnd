package com.Camello.Camello.service;

import com.Camello.Camello.dto.ConversationDto;
import com.Camello.Camello.dto.MessageDto;
import com.Camello.Camello.dto.SendMessageRequest;
import com.Camello.Camello.entity.Conversation;
import com.Camello.Camello.entity.Message;
import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.ConversationRepository;
import com.Camello.Camello.repository.MessageRepository;
import com.Camello.Camello.repository.UserRepository;
import com.Camello.Camello.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    public MessageDto sendMessage(UUID senderId, SendMessageRequest request) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario remitente no encontrado"));
        
        User recipient = userRepository.findById(request.getRecipientId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario destinatario no encontrado"));
        
        // Buscar o crear conversación
        Conversation conversation = conversationRepository.findByParticipants(sender, recipient)
            .orElseGet(() -> createConversation(sender, recipient));
        
        // Crear mensaje
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setAttachments(request.getAttachments());
        message.setIsRead(false);
        
        message = messageRepository.save(message);
        
        // Actualizar conversación
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);
        
        // Enviar notificación
        notificationService.createNotification(
            recipient.getId(),
            "NEW_MESSAGE",
            "Nuevo mensaje",
            "Has recibido un nuevo mensaje de " + getSenderName(sender),
            null
        );
        
        return convertMessageToDto(message);
    }
    
    @Transactional(readOnly = true)
    public Page<ConversationDto> getUserConversations(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        return conversationRepository.findByParticipantOrderByUpdatedAtDesc(user, pageable)
            .map(conversation -> convertConversationToDto(conversation, user));
    }
    
    @Transactional(readOnly = true)
    public Page<MessageDto> getConversationMessages(UUID conversationId, UUID userId, Pageable pageable) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        // Verificar que el usuario es participante de la conversación
        if (!conversation.getParticipant1().getId().equals(userId) && 
            !conversation.getParticipant2().getId().equals(userId)) {
            throw new IllegalArgumentException("No tienes acceso a esta conversación");
        }
        
        return messageRepository.findByConversationOrderBySentAtDesc(conversation, pageable)
            .map(this::convertMessageToDto);
    }
    
    public void markMessagesAsRead(UUID conversationId, UUID userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        // Verificar que el usuario es participante de la conversación
        if (!conversation.getParticipant1().getId().equals(userId) && 
            !conversation.getParticipant2().getId().equals(userId)) {
            throw new IllegalArgumentException("No tienes acceso a esta conversación");
        }
        
        messageRepository.markMessagesAsRead(conversation, user);
    }
    
    @Transactional(readOnly = true)
    public Long getUnreadMessagesCount(UUID conversationId, UUID userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        return messageRepository.countUnreadMessages(conversation, user);
    }
    
    private Conversation createConversation(User participant1, User participant2) {
        Conversation conversation = new Conversation();
        conversation.setParticipant1(participant1);
        conversation.setParticipant2(participant2);
        return conversationRepository.save(conversation);
    }
    
    private String getSenderName(User sender) {
        // Intentar obtener el nombre del perfil
        try {
            if (sender.getRole() == User.UserRole.FREELANCER) {
                // Aquí podrías inyectar FreelancerProfileService si necesitas el nombre completo
                return "Freelancer"; // Placeholder
            } else if (sender.getRole() == User.UserRole.CONTRACTOR) {
                return "Contratante"; // Placeholder
            }
        } catch (Exception e) {
            // Fallback al email
        }
        return sender.getEmail();
    }
    
    private MessageDto convertMessageToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setConversationId(message.getConversation().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(getSenderName(message.getSender()));
        // dto.setSenderProfilePicture() - Se puede agregar después
        dto.setContent(message.getContent());
        dto.setAttachments(message.getAttachments());
        dto.setIsRead(message.getIsRead());
        dto.setSentAt(message.getSentAt());
        return dto;
    }
    
    private ConversationDto convertConversationToDto(Conversation conversation, User currentUser) {
        ConversationDto dto = new ConversationDto();
        dto.setId(conversation.getId());
        
        // Determinar el otro participante
        User otherParticipant = conversation.getParticipant1().getId().equals(currentUser.getId()) 
            ? conversation.getParticipant2() : conversation.getParticipant1();
        
        dto.setParticipant1Id(conversation.getParticipant1().getId());
        dto.setParticipant1Name(getSenderName(conversation.getParticipant1()));
        dto.setParticipant2Id(conversation.getParticipant2().getId());
        dto.setParticipant2Name(getSenderName(conversation.getParticipant2()));
        
        // Obtener último mensaje (esto se puede optimizar con una query)
        dto.setLastMessage(""); // Placeholder
        dto.setLastMessageAt(conversation.getUpdatedAt());
        
        // Contar mensajes no leídos
        dto.setUnreadCount(messageRepository.countUnreadMessages(conversation, currentUser).intValue());
        
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setUpdatedAt(conversation.getUpdatedAt());
        
        return dto;
    }
}