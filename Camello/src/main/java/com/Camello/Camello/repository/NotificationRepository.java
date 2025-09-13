package com.Camello.Camello.repository;

import com.Camello.Camello.entity.Notification;
import com.Camello.Camello.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user = :user AND n.isRead = false")
    Long countUnreadNotifications(@Param("user") User user);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user = :user")
    void markAllAsRead(@Param("user") User user);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id AND n.user = :user")
    void markAsRead(@Param("id") UUID id, @Param("user") User user);
}