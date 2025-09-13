package com.Camello.Camello.service;

import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.UserRepository;
import com.Camello.Camello.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    // Métodos existentes
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
    
    public List<User> getUsersByRole(User.UserRole role) {
        return userRepository.findByRoleAndActive(role);
    }
    
    public Long countUsersByRole(User.UserRole role) {
        return userRepository.countByRoleAndActive(role);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // Nuevos métodos para administración
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public User getUserByIdRequired(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
    
    @Transactional(readOnly = true)
    public User getUserByEmailRequired(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
    
    public User activateUser(UUID userId) {
        User user = getUserByIdRequired(userId);
        user.setIsActive(true);
        return userRepository.save(user);
    }
    
    public User deactivateUser(UUID userId) {
        User user = getUserByIdRequired(userId);
        user.setIsActive(false);
        return userRepository.save(user);
    }
    
    public void deleteUser(UUID userId) {
        User user = getUserByIdRequired(userId);
        user.setDeletedAt(LocalDateTime.now());
        user.setIsActive(false);
        userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public Long getTotalUsers() {
        return userRepository.count();
    }
    
    @Transactional(readOnly = true)
    public Long getTotalFreelancers() {
        return userRepository.countByRole(User.UserRole.FREELANCER);
    }
    
    @Transactional(readOnly = true)
    public Long getTotalContractors() {
        return userRepository.countByRole(User.UserRole.CONTRACTOR);
    }
    
    @Transactional(readOnly = true)
    public Long getActiveUsers() {
        return userRepository.countByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public Page<User> searchUsers(String email, User.UserRole role, Boolean isActive, Pageable pageable) {
        if (email != null && role != null && isActive != null) {
            return userRepository.findByEmailContainingIgnoreCaseAndRoleAndIsActive(email, role, isActive, pageable);
        } else if (email != null && role != null) {
            return userRepository.findByEmailContainingIgnoreCaseAndRole(email, role, pageable);
        } else if (email != null && isActive != null) {
            return userRepository.findByEmailContainingIgnoreCaseAndIsActive(email, isActive, pageable);
        } else if (role != null && isActive != null) {
            return userRepository.findByRoleAndIsActive(role, isActive, pageable);
        } else if (email != null) {
            return userRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else if (role != null) {
            return userRepository.findByRole(role, pageable);
        } else if (isActive != null) {
            return userRepository.findByIsActive(isActive, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }
} 