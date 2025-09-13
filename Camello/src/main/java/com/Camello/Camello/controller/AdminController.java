package com.Camello.Camello.controller;

import com.Camello.Camello.entity.User;
import com.Camello.Camello.exception.ResourceNotFoundException;
import com.Camello.Camello.service.UserService;
import com.Camello.Camello.service.ServiceService;
import com.Camello.Camello.service.JobOfferService;
import com.Camello.Camello.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final UserService userService;
    
    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable UUID userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<User> activateUser(@PathVariable UUID userId) {
        User user = userService.activateUser(userId);
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable UUID userId) {
        User user = userService.deactivateUser(userId);
        return ResponseEntity.ok(user);
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Estadísticas básicas
        stats.put("totalUsers", userService.getTotalUsers());
        stats.put("totalFreelancers", userService.getTotalFreelancers());
        stats.put("totalContractors", userService.getTotalContractors());
        stats.put("activeUsers", userService.getActiveUsers());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/users/search")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) User.UserRole role,
            @RequestParam(required = false) Boolean isActive,
            Pageable pageable) {
        
        Page<User> users = userService.searchUsers(email, role, isActive, pageable);
        return ResponseEntity.ok(users);
    }
}