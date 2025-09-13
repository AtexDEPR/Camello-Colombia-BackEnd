package com.Camello.Camello.controller;

import com.Camello.Camello.entity.Review;
import com.Camello.Camello.service.ReviewService;
import com.Camello.Camello.service.UserContextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {
    
    private final ReviewService reviewService;
    private final UserContextService userContextService;
    
    @PostMapping
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Review> createReview(
            @RequestParam UUID contractId,
            @RequestParam UUID revieweeId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment,
            @RequestHeader("Authorization") String token) {
        
        UUID reviewerId = userContextService.getUserIdFromToken(token);
        Review review = reviewService.createReview(contractId, reviewerId, revieweeId, rating, comment);
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Review>> getUserReviews(
            @PathVariable UUID userId,
            Pageable pageable) {
        
        Page<Review> reviews = reviewService.getReviewsByUser(userId, pageable);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/user/{userId}/average-rating")
    public ResponseEntity<Double> getUserAverageRating(@PathVariable UUID userId) {
        Double averageRating = reviewService.getUserAverageRating(userId);
        return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
    }
    
    @GetMapping("/user/{userId}/total-reviews")
    public ResponseEntity<Long> getUserTotalReviews(@PathVariable UUID userId) {
        Long totalReviews = reviewService.getUserTotalReviews(userId);
        return ResponseEntity.ok(totalReviews != null ? totalReviews : 0L);
    }
    
    @GetMapping("/contract/{contractId}/can-review")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Boolean> canUserReview(
            @PathVariable UUID contractId,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        boolean canReview = reviewService.canUserReview(contractId, userId);
        return ResponseEntity.ok(canReview);
    }
}