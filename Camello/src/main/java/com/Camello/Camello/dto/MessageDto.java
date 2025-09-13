package com.Camello.Camello.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String senderName;
    private String senderProfilePicture;
    private String content;
    private List<String> attachments;
    private Boolean isRead;
    private LocalDateTime sentAt;
}