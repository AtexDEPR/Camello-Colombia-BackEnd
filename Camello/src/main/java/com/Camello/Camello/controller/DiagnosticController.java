package com.Camello.Camello.controller;

import com.Camello.Camello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diagnostic")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DiagnosticController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/simple")
    public String simple() {
        return "Simple endpoint working";
    }

    @PostMapping("/echo")
    public String echo(@RequestBody String body) {
        return "Echo: " + body;
    }

    @GetMapping("/test-db")
    public String testDatabase() {
        try {
            long userCount = userRepository.count();
            return "Database connection OK. Users in database: " + userCount;
        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    @GetMapping("/check-user/{email}")
    public String checkUser(@PathVariable String email) {
        try {
            boolean exists = userRepository.existsByEmail(email);
            if (exists) {
                var user = userRepository.findByEmail(email);
                return "User exists: " + email + ", Active: " + user.get().getIsActive() + 
                       ", Hash: " + user.get().getPasswordHash();
            } else {
                return "User does not exist: " + email;
            }
        } catch (Exception e) {
            return "Error checking user: " + e.getMessage();
        }
    }
    
    @GetMapping("/test-password/{email}/{password}")
    public String testPassword(@PathVariable String email, @PathVariable String password) {
        try {
            var userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                var user = userOpt.get();
                boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
                return "Password test for " + email + ": " + matches + 
                       ", Provided: " + password + 
                       ", Stored hash: " + user.getPasswordHash();
            } else {
                return "User not found: " + email;
            }
        } catch (Exception e) {
            return "Error testing password: " + e.getMessage();
        }
    }
    
    @PostMapping("/fix-passwords")
    public String fixPasswords() {
        try {
            // Obtener todos los usuarios con hashes truncados
            var users = userRepository.findAll();
            int fixed = 0;
            
            for (var user : users) {
                if (user.getPasswordHash().length() < 60) {
                    // Actualizar con el hash correcto para "password123"
                    user.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6");
                    userRepository.save(user);
                    fixed++;
                }
            }
            
            return "Fixed " + fixed + " users with truncated password hashes";
        } catch (Exception e) {
            return "Error fixing passwords: " + e.getMessage();
        }
    }
}