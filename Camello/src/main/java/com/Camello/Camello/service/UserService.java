package com.Camello.Camello.service;

import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActive();
    }
    
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public void deactivateUser(UUID userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setIsActive(false);
            userRepository.save(user);
        });
    }
    
    public void deleteUser(UUID userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setDeletedAt(java.time.LocalDateTime.now());
            userRepository.save(user);
        });
    }
    
    public List<User> getUsersByRole(User.UserRole role) {
        return userRepository.findByRoleAndActive(role);
    }
    
    public Long countUsersByRole(User.UserRole role) {
        return userRepository.countByRoleAndActive(role);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
} 