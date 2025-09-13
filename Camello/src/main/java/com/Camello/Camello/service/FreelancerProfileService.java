package com.Camello.Camello.service;

import com.Camello.Camello.dto.FreelancerProfileDto;
import com.Camello.Camello.dto.UpdateFreelancerProfileRequest;
import com.Camello.Camello.entity.FreelancerProfile;
import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.FreelancerProfileRepository;
import com.Camello.Camello.repository.UserRepository;
import com.Camello.Camello.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FreelancerProfileService {
    
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final UserRepository userRepository;
    
    public FreelancerProfileDto createProfile(UUID userId, UpdateFreelancerProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        if (user.getRole() != User.UserRole.FREELANCER) {
            throw new IllegalArgumentException("El usuario no es un freelancer");
        }
        
        FreelancerProfile profile = new FreelancerProfile();
        profile.setUser(user);
        updateProfileFromRequest(profile, request);
        
        profile = freelancerProfileRepository.save(profile);
        return convertToDto(profile);
    }
    
    @Transactional(readOnly = true)
    public FreelancerProfileDto getProfileByUserId(UUID userId) {
        FreelancerProfile profile = freelancerProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de freelancer no encontrado"));
        
        return convertToDto(profile);
    }
    
    @Transactional(readOnly = true)
    public FreelancerProfileDto getProfileById(UUID profileId) {
        FreelancerProfile profile = freelancerProfileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de freelancer no encontrado"));
        
        return convertToDto(profile);
    }
    
    public FreelancerProfileDto updateProfile(UUID userId, UpdateFreelancerProfileRequest request) {
        FreelancerProfile profile = freelancerProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de freelancer no encontrado"));
        
        updateProfileFromRequest(profile, request);
        profile = freelancerProfileRepository.save(profile);
        
        return convertToDto(profile);
    }
    
    @Transactional(readOnly = true)
    public Page<FreelancerProfileDto> searchFreelancers(String query, String location, 
                                                       BigDecimal minRate, BigDecimal maxRate, 
                                                       String availability, Pageable pageable) {
        
        Page<FreelancerProfile> profiles;
        
        if (query != null && !query.trim().isEmpty()) {
            profiles = freelancerProfileRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrTitleContainingIgnoreCase(
                query, query, query, pageable);
        } else {
            profiles = freelancerProfileRepository.findAll(pageable);
        }
        
        return profiles.map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<FreelancerProfileDto> getTopRatedFreelancers(Pageable pageable) {
        return freelancerProfileRepository.findByOrderByRatingDescTotalReviewsDesc(pageable)
            .map(this::convertToDto);
    }
    
    public void updateRating(UUID freelancerId, BigDecimal newRating, Integer totalReviews) {
        FreelancerProfile profile = freelancerProfileRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de freelancer no encontrado"));
        
        profile.setRating(newRating);
        profile.setTotalReviews(totalReviews);
        freelancerProfileRepository.save(profile);
    }
    
    public void updateEarnings(UUID freelancerId, BigDecimal additionalEarnings) {
        FreelancerProfile profile = freelancerProfileRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de freelancer no encontrado"));
        
        BigDecimal currentEarnings = profile.getTotalEarnings() != null ? profile.getTotalEarnings() : BigDecimal.ZERO;
        profile.setTotalEarnings(currentEarnings.add(additionalEarnings));
        freelancerProfileRepository.save(profile);
    }
    
    private void updateProfileFromRequest(FreelancerProfile profile, UpdateFreelancerProfileRequest request) {
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setProfilePicture(request.getProfilePicture());
        profile.setTitle(request.getTitle());
        profile.setDescription(request.getDescription());
        profile.setLocation(request.getLocation());
        profile.setPhone(request.getPhone());
        profile.setSkills(request.getSkills());
        profile.setPortfolioUrls(request.getPortfolioUrls());
        profile.setHourlyRate(request.getHourlyRate());
        
        if (request.getAvailability() != null) {
            profile.setAvailability(FreelancerProfile.Availability.valueOf(request.getAvailability()));
        }
    }
    
    private FreelancerProfileDto convertToDto(FreelancerProfile profile) {
        FreelancerProfileDto dto = new FreelancerProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setProfilePicture(profile.getProfilePicture());
        dto.setTitle(profile.getTitle());
        dto.setDescription(profile.getDescription());
        dto.setLocation(profile.getLocation());
        dto.setPhone(profile.getPhone());
        dto.setSkills(profile.getSkills());
        dto.setPortfolioUrls(profile.getPortfolioUrls());
        dto.setHourlyRate(profile.getHourlyRate());
        dto.setAvailability(profile.getAvailability() != null ? profile.getAvailability().name() : null);
        dto.setRating(profile.getRating());
        dto.setTotalReviews(profile.getTotalReviews());
        dto.setTotalEarnings(profile.getTotalEarnings());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }
}