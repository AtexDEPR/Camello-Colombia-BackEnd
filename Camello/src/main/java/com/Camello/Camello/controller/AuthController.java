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
        // TODO: Implementar refresh token
        return ResponseEntity.ok(new AuthResponse("Refresh token implementado", true));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        // TODO: Implementar logout (invalidar token)
        return ResponseEntity.ok(new AuthResponse("Logout exitoso", true));
    }
} 