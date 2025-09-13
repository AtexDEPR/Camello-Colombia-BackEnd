package com.Camello.Camello.dto;

import com.Camello.Camello.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private String refreshToken;
    private UUID userId;
    private String email;
    private User.UserRole role;
    private String message;
    private boolean success;
    
    public AuthResponse(String token, String refreshToken, User user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.success = true;
        this.message = "Autenticación exitosa";
    }
    
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
} 