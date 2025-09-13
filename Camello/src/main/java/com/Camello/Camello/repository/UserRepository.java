package com.Camello.Camello.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Camello.Camello.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailAndIsActiveTrue(String email);
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
    List<User> findByRoleAndActive(@Param("role") User.UserRole role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isActive = true")
    Long countByRoleAndActive(@Param("role") User.UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.deletedAt IS NULL")
    List<User> findAllActive();
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true AND u.deletedAt IS NULL")
    Optional<User> findActiveByEmail(@Param("email") String email);
    
    // Métodos adicionales para administración
    Long countByRole(User.UserRole role);
    
    Long countByIsActiveTrue();
    
    org.springframework.data.domain.Page<User> findByEmailContainingIgnoreCaseAndRoleAndIsActive(String email, User.UserRole role, Boolean isActive, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<User> findByEmailContainingIgnoreCaseAndRole(String email, User.UserRole role, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<User> findByEmailContainingIgnoreCaseAndIsActive(String email, Boolean isActive, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<User> findByRoleAndIsActive(User.UserRole role, Boolean isActive, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<User> findByEmailContainingIgnoreCase(String email, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<User> findByRole(User.UserRole role, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<User> findByIsActive(Boolean isActive, org.springframework.data.domain.Pageable pageable);
}