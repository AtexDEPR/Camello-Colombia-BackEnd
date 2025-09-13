package com.Camello.Camello.controller;

import com.Camello.Camello.dto.AuthRequest;
import com.Camello.Camello.dto.AuthResponse;
import com.Camello.Camello.dto.RegisterRequest;
import com.Camello.Camello.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthenticationService authenticationService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String refreshToken = token.replace("Bearer ", "");
            AuthResponse response = authenticationService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Token inválido", false));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            authenticationService.logout(jwt);
            return ResponseEntity.ok(new AuthResponse("Logout exitoso", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Error en logout", false));
        }
    }
} 