package com.Camello.Camello.service;

import com.Camello.Camello.entity.Notification;
import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.NotificationRepository;
import com.Camello.Camello.repository.UserRepository;
import com.Camello.Camello.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    
    public Notification createNotification(UUID userId, String type, String title, String message, String data) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setData(data);
        notification.setIsRead(false);
        
        return notificationRepository.save(notification);
    }
    
    @Transactional(readOnly = true)
    public Page<Notification> getUserNotifications(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        return notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }
    
    @Transactional(readOnly = true)
    public Long getUnreadNotificationsCount(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        return notificationRepository.countUnreadNotifications(user);
    }
    
    public void markNotificationAsRead(UUID notificationId, UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        notificationRepository.markAsRead(notificationId, user);
    }
    
    public void markAllNotificationsAsRead(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        notificationRepository.markAllAsRead(user);
    }
}