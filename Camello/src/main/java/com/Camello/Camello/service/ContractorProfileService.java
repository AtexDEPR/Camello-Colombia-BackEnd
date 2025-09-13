package com.Camello.Camello.service;

import com.Camello.Camello.dto.ContractorProfileDto;
import com.Camello.Camello.dto.UpdateContractorProfileRequest;
import com.Camello.Camello.entity.ContractorProfile;
import com.Camello.Camello.entity.User;
import com.Camello.Camello.repository.ContractorProfileRepository;
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
public class ContractorProfileService {
    
    private final ContractorProfileRepository contractorProfileRepository;
    private final UserRepository userRepository;
    
    public ContractorProfileDto createProfile(UUID userId, UpdateContractorProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        if (user.getRole() != User.UserRole.CONTRACTOR) {
            throw new IllegalArgumentException("El usuario no es un contratante");
        }
        
        ContractorProfile profile = new ContractorProfile();
        profile.setUser(user);
        updateProfileFromRequest(profile, request);
        
        profile = contractorProfileRepository.save(profile);
        return convertToDto(profile);
    }
    
    @Transactional(readOnly = true)
    public ContractorProfileDto getProfileByUserId(UUID userId) {
        ContractorProfile profile = contractorProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de contratante no encontrado"));
        
        return convertToDto(profile);
    }
    
    @Transactional(readOnly = true)
    public ContractorProfileDto getProfileById(UUID profileId) {
        ContractorProfile profile = contractorProfileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de contratante no encontrado"));
        
        return convertToDto(profile);
    }
    
    public ContractorProfileDto updateProfile(UUID userId, UpdateContractorProfileRequest request) {
        ContractorProfile profile = contractorProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de contratante no encontrado"));
        
        updateProfileFromRequest(profile, request);
        profile = contractorProfileRepository.save(profile);
        
        return convertToDto(profile);
    }
    
    @Transactional(readOnly = true)
    public Page<ContractorProfileDto> searchContractors(String query, String location, 
                                                       String industry, Pageable pageable) {
        
        Page<ContractorProfile> profiles;
        
        if (query != null && !query.trim().isEmpty()) {
            profiles = contractorProfileRepository.findByCompanyNameContainingIgnoreCaseOrContactNameContainingIgnoreCase(
                query, query, pageable);
        } else {
            profiles = contractorProfileRepository.findAll(pageable);
        }
        
        return profiles.map(this::convertToDto);
    }
    
    public void updateRating(UUID contractorId, BigDecimal newRating, Integer totalReviews) {
        ContractorProfile profile = contractorProfileRepository.findById(contractorId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de contratante no encontrado"));
        
        profile.setRating(newRating);
        profile.setTotalReviews(totalReviews);
        contractorProfileRepository.save(profile);
    }
    
    public void updateSpent(UUID contractorId, BigDecimal additionalSpent) {
        ContractorProfile profile = contractorProfileRepository.findById(contractorId)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de contratante no encontrado"));
        
        BigDecimal currentSpent = profile.getTotalSpent() != null ? profile.getTotalSpent() : BigDecimal.ZERO;
        profile.setTotalSpent(currentSpent.add(additionalSpent));
        contractorProfileRepository.save(profile);
    }
    
    private void updateProfileFromRequest(ContractorProfile profile, UpdateContractorProfileRequest request) {
        profile.setCompanyName(request.getCompanyName());
        profile.setContactName(request.getContactName());
        profile.setProfilePicture(request.getProfilePicture());
        profile.setDescription(request.getDescription());
        profile.setLocation(request.getLocation());
        profile.setPhone(request.getPhone());
        profile.setWebsite(request.getWebsite());
        profile.setIndustry(request.getIndustry());
        
        if (request.getCompanySize() != null) {
            profile.setCompanySize(ContractorProfile.CompanySize.valueOf(request.getCompanySize()));
        }
    }
    
    private ContractorProfileDto convertToDto(ContractorProfile profile) {
        ContractorProfileDto dto = new ContractorProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setCompanyName(profile.getCompanyName());
        dto.setContactName(profile.getContactName());
        dto.setProfilePicture(profile.getProfilePicture());
        dto.setDescription(profile.getDescription());
        dto.setLocation(profile.getLocation());
        dto.setPhone(profile.getPhone());
        dto.setWebsite(profile.getWebsite());
        dto.setIndustry(profile.getIndustry());
        dto.setCompanySize(profile.getCompanySize() != null ? profile.getCompanySize().name() : null);
        dto.setRating(profile.getRating());
        dto.setTotalReviews(profile.getTotalReviews());
        dto.setTotalSpent(profile.getTotalSpent());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }
}