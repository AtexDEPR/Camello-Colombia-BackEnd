package com.Camello.Camello.repository;

import com.Camello.Camello.entity.Message;
import com.Camello.Camello.entity.Conversation;
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
public interface MessageRepository extends JpaRepository<Message, UUID> {
    
    Page<Message> findByConversationOrderBySentAtDesc(Conversation conversation, Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation = :conversation AND m.sender != :user AND m.isRead = false")
    Long countUnreadMessages(@Param("conversation") Conversation conversation, @Param("user") User user);
    
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.conversation = :conversation AND m.sender != :user")
    void markMessagesAsRead(@Param("conversation") Conversation conversation, @Param("user") User user);
}