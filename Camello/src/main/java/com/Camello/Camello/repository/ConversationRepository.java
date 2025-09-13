package com.Camello.Camello.repository;

import com.Camello.Camello.entity.Conversation;
import com.Camello.Camello.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    
    @Query("SELECT c FROM Conversation c WHERE (c.participant1 = :user OR c.participant2 = :user) ORDER BY c.updatedAt DESC")
    Page<Conversation> findByParticipantOrderByUpdatedAtDesc(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT c FROM Conversation c WHERE (c.participant1 = :user1 AND c.participant2 = :user2) OR (c.participant1 = :user2 AND c.participant2 = :user1)")
    Optional<Conversation> findByParticipants(@Param("user1") User user1, @Param("user2") User user2);
}