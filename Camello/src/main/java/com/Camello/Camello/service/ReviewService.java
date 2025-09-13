package com.Camello.Camello.service;

import com.Camello.Camello.entity.Review;
import com.Camello.Camello.entity.Contract;
import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.ReviewRepository;
import com.Camello.Camello.repository.ContractRepository;
import com.Camello.Camello.repository.UserRepository;
import com.Camello.Camello.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final FreelancerProfileService freelancerProfileService;
    private final ContractorProfileService contractorProfileService;
    private final NotificationService notificationService;
    
    public Review createReview(UUID contractId, UUID reviewerId, UUID revieweeId, 
                              Integer rating, String comment) {
        
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contrato no encontrado"));
        
        User reviewer = userRepository.findById(reviewerId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario revisor no encontrado"));
        
        User reviewee = userRepository.findById(revieweeId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario a revisar no encontrado"));
        
        // Verificar que el contrato esté completado
        if (contract.getStatus() != Contract.ContractStatus.COMPLETED) {
            throw new IllegalArgumentException("Solo se pueden crear reseñas para contratos completados");
        }
        
        // Verificar que el revisor sea parte del contrato
        boolean isFreelancer = contract.getFreelancer().getUser().getId().equals(reviewerId);
        boolean isContractor = contract.getContractor().getUser().getId().equals(reviewerId);
        
        if (!isFreelancer && !isContractor) {
            throw new IllegalArgumentException("Solo los participantes del contrato pueden crear reseñas");
        }
        
        // Verificar que no haya reseña previa del mismo revisor para este contrato
        if (reviewRepository.existsByContractAndReviewer(contract, reviewer)) {
            throw new IllegalArgumentException("Ya has creado una reseña para este contrato");
        }
        
        // Validar rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas");
        }
        
        Review review = new Review();
        review.setContract(contract);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewerType(isFreelancer ? Review.ReviewerType.FREELANCER : Review.ReviewerType.CONTRACTOR);
        
        review = reviewRepository.save(review);
        
        // Actualizar calificación promedio del usuario reseñado
        updateUserRating(revieweeId);
        
        // Enviar notificación
        notificationService.createNotification(
            revieweeId,
            "NEW_REVIEW",
            "Nueva reseña recibida",
            "Has recibido una nueva reseña de " + rating + " estrellas",
            null
        );
        
        return review;
    }
    
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByUser(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        return reviewRepository.findByRevieweeOrderByCreatedAtDesc(user, pageable);
    }
    
    @Transactional(readOnly = true)
    public Double getUserAverageRating(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        return reviewRepository.getAverageRatingByUser(user);
    }
    
    @Transactional(readOnly = true)
    public Long getUserTotalReviews(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        return reviewRepository.getTotalReviewsByUser(user);
    }
    
    @Transactional(readOnly = true)
    public boolean canUserReview(UUID contractId, UUID userId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contrato no encontrado"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        // Verificar que el contrato esté completado
        if (contract.getStatus() != Contract.ContractStatus.COMPLETED) {
            return false;
        }
        
        // Verificar que el usuario sea parte del contrato
        boolean isParticipant = contract.getFreelancer().getUser().getId().equals(userId) ||
                               contract.getContractor().getUser().getId().equals(userId);
        
        if (!isParticipant) {
            return false;
        }
        
        // Verificar que no haya reseña previa
        return !reviewRepository.existsByContractAndReviewer(contract, user);
    }
    
    private void updateUserRating(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        Double averageRating = reviewRepository.getAverageRatingByUser(user);
        Long totalReviews = reviewRepository.getTotalReviewsByUser(user);
        
        if (averageRating != null && totalReviews != null) {
            BigDecimal rating = BigDecimal.valueOf(averageRating).setScale(2, RoundingMode.HALF_UP);
            
            if (user.getRole() == User.UserRole.FREELANCER) {
                try {
                    var freelancerProfile = freelancerProfileService.getProfileByUserId(userId);
                    freelancerProfileService.updateRating(freelancerProfile.getId(), rating, totalReviews.intValue());
                } catch (Exception e) {
                    // Profile might not exist yet
                }
            } else if (user.getRole() == User.UserRole.CONTRACTOR) {
                try {
                    var contractorProfile = contractorProfileService.getProfileByUserId(userId);
                    contractorProfileService.updateRating(contractorProfile.getId(), rating, totalReviews.intValue());
                } catch (Exception e) {
                    // Profile might not exist yet
                }
            }
        }
    }
}