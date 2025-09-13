package com.Camello.Camello.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    
    @NotNull(message = "El destinatario es obligatorio")
    private UUID recipientId;
    
    @NotBlank(message = "El contenido del mensaje es obligatorio")
    @Size(min = 1, max = 2000, message = "El mensaje debe tener entre 1 y 2000 caracteres")
    private String content;
    
    @Size(max = 5, message = "Máximo 5 archivos adjuntos por mensaje")
    private List<String> attachments;
}