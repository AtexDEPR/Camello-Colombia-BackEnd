package com.Camello.Camello.service;

import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.UserRepository;
import com.Camello.Camello.security.JwtService;
import com.Camello.Camello.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserContextService {
    
    private final JwtService jwtService;
    private final UserRepository userRepository;
    
    public UUID getUserIdFromToken(String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return user.getId();
    }
    
    public User getUserFromToken(String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}