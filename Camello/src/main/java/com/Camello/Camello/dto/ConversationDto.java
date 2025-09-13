package com.Camello.Camello.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDto {
    private UUID id;
    private UUID participant1Id;
    private String participant1Name;
    private String participant1ProfilePicture;
    private UUID participant2Id;
    private String participant2Name;
    private String participant2ProfilePicture;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private Integer unreadCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}