package com.Camello.Camello.repository;

import com.Camello.Camello.entity.Review;
import com.Camello.Camello.entity.User;
import com.Camello.Camello.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    
    Page<Review> findByRevieweeOrderByCreatedAtDesc(User reviewee, Pageable pageable);
    
    Optional<Review> findByContractAndReviewer(Contract contract, User reviewer);
    
    boolean existsByContractAndReviewer(Contract contract, User reviewer);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewee = :user")
    Double getAverageRatingByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewee = :user")
    Long getTotalReviewsByUser(@Param("user") User user);
}