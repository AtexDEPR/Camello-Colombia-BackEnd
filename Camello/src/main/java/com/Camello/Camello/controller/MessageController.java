package com.Camello.Camello.controller;

import com.Camello.Camello.dto.ConversationDto;
import com.Camello.Camello.dto.MessageDto;
import com.Camello.Camello.dto.SendMessageRequest;
import com.Camello.Camello.service.MessageService;
import com.Camello.Camello.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {
    
    private final MessageService messageService;
    private final JwtService jwtService;
    
    @PostMapping
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<MessageDto> sendMessage(
            @Valid @RequestBody SendMessageRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID senderId = getUserIdFromToken(token);
        MessageDto message = messageService.sendMessage(senderId, request);
        return ResponseEntity.ok(message);
    }
    
    @GetMapping("/conversations")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Page<ConversationDto>> getUserConversations(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        UUID userId = getUserIdFromToken(token);
        Page<ConversationDto> conversations = messageService.getUserConversations(userId, pageable);
        return ResponseEntity.ok(conversations);
    }
    
    @GetMapping("/conversations/{conversationId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Page<MessageDto>> getConversationMessages(
            @PathVariable UUID conversationId,
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        UUID userId = getUserIdFromToken(token);
        Page<MessageDto> messages = messageService.getConversationMessages(conversationId, userId, pageable);
        return ResponseEntity.ok(messages);
    }
    
    @PutMapping("/conversations/{conversationId}/read")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable UUID conversationId,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        messageService.markMessagesAsRead(conversationId, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/conversations/{conversationId}/unread-count")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Long> getUnreadMessagesCount(
            @PathVariable UUID conversationId,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        Long count = messageService.getUnreadMessagesCount(conversationId, userId);
        return ResponseEntity.ok(count);
    }
    
    private UUID getUserIdFromToken(String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        // Placeholder - necesitarías implementar la obtención del userId
        return UUID.randomUUID();
    }
}