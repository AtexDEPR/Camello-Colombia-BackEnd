package com.Camello.Camello.controller;

import com.Camello.Camello.entity.Notification;
import com.Camello.Camello.service.NotificationService;
import com.Camello.Camello.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {
    
    private final NotificationService notificationService;
    private final UserContextService userContextService;
    
    @GetMapping
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Page<Notification>> getUserNotifications(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        Page<Notification> notifications = notificationService.getUserNotifications(userId, pageable);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread-count")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Long> getUnreadNotificationsCount(@RequestHeader("Authorization") String token) {
        UUID userId = userContextService.getUserIdFromToken(token);
        Long count = notificationService.getUnreadNotificationsCount(userId);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Void> markNotificationAsRead(
            @PathVariable UUID notificationId,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        notificationService.markNotificationAsRead(notificationId, userId);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/read-all")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Void> markAllNotificationsAsRead(@RequestHeader("Authorization") String token) {
        UUID userId = userContextService.getUserIdFromToken(token);
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.ok().build();
    }
}