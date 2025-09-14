package com.Camello.Camello.security;

import com.Camello.Camello.dto.AuthRequest;
import com.Camello.Camello.dto.AuthResponse;
import com.Camello.Camello.dto.RegisterRequest;
import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AuthResponse register(RegisterRequest request) {
        try {
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
            
            // Generar token simple (temporal)
            String token = "simple-token-" + user.getId();
            
            return new AuthResponse(token, token, user);
            
        } catch (Exception e) {
            return new AuthResponse("Error en registro: " + e.getMessage(), false);
        }
    }
    
    public AuthResponse authenticate(AuthRequest request) {
        try {
            System.out.println("🔍 Intentando autenticar usuario: " + request.getEmail());
            
            // Buscar usuario
            User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);
            
            if (user == null) {
                System.out.println("❌ Usuario no encontrado: " + request.getEmail());
                return new AuthResponse("Usuario no encontrado", false);
            }
            
            System.out.println("✅ Usuario encontrado: " + user.getEmail() + ", Activo: " + user.getIsActive());
            
            if (!user.getIsActive()) {
                return new AuthResponse("Cuenta desactivada", false);
            }
            
            // Verificar password (simplificado)
            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
            System.out.println("🔐 Password matches: " + passwordMatches);
            
            if (passwordMatches) {
                // Generar token simple (temporal)
                String token = "simple-token-" + user.getId();
                
                System.out.println("🎉 Autenticación exitosa para: " + user.getEmail());
                return new AuthResponse(token, token, user);
            } else {
                System.out.println("❌ Credenciales inválidas para: " + user.getEmail());
                return new AuthResponse("Credenciales inválidas", false);
            }
            
        } catch (Exception e) {
            System.out.println("💥 Error en autenticación: " + e.getMessage());
            e.printStackTrace();
            return new AuthResponse("Error en autenticación: " + e.getMessage(), false);
        }
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        return new AuthResponse("Refresh token no implementado temporalmente", false);
    }
    
    public void logout(String token) {
        // Logout simple - no hacer nada por ahora
    }
}