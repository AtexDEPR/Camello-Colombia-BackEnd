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
    private User user;
    private long expiresIn;
    private String message;
    private boolean success;
    
    public AuthResponse(String token, String refreshToken, User user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
        this.expiresIn = 86400000; // 24 horas en milisegundos
        this.success = true;
        this.message = "Autenticación exitosa";
    }
    
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.token = null;
        this.refreshToken = null;
        this.user = null;
        this.expiresIn = 0;
    }
} 