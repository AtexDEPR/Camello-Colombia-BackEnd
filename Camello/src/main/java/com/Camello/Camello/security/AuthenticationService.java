package com.Camello.Camello.security;

import com.Camello.Camello.dto.AuthRequest;
import com.Camello.Camello.dto.AuthResponse;
import com.Camello.Camello.dto.RegisterRequest;
import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("El email ya está registrado", false);
        }
        
        // Verificar que las contraseñas coincidan
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new AuthResponse("Las contraseñas no coinciden", false);
        }
        
        // Crear el usuario
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setIsActive(true);
        user.setIsVerified(false);
        
        userRepository.save(user);
        
        // Generar tokens
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        return new AuthResponse(jwtToken, refreshToken, user);
    }
    
    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
            
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            if (!user.getIsActive()) {
                return new AuthResponse("Cuenta desactivada", false);
            }
            
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            
            return new AuthResponse(jwtToken, refreshToken, user);
            
        } catch (Exception e) {
            return new AuthResponse("Credenciales inválidas", false);
        }
    }
} 