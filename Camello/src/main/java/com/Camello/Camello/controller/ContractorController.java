package com.Camello.Camello.controller;

import com.Camello.Camello.dto.ContractorProfileDto;
import com.Camello.Camello.dto.UpdateContractorProfileRequest;
import com.Camello.Camello.dto.JobOfferDto;
import com.Camello.Camello.dto.CreateJobOfferRequest;
import com.Camello.Camello.service.ContractorProfileService;
import com.Camello.Camello.service.JobOfferService;
import com.Camello.Camello.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/contractors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContractorController {
    
    private final ContractorProfileService contractorProfileService;
    private final JobOfferService jobOfferService;
    private final JwtService jwtService;
    
    @PostMapping("/profile")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<ContractorProfileDto> createProfile(
            @Valid @RequestBody UpdateContractorProfileRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        ContractorProfileDto profile = contractorProfileService.createProfile(userId, request);
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<ContractorProfileDto> getMyProfile(@RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        ContractorProfileDto profile = contractorProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<ContractorProfileDto> getProfile(@PathVariable UUID profileId) {
        ContractorProfileDto profile = contractorProfileService.getProfileById(profileId);
        return ResponseEntity.ok(profile);
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<ContractorProfileDto> updateProfile(
            @Valid @RequestBody UpdateContractorProfileRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        ContractorProfileDto profile = contractorProfileService.updateProfile(userId, request);
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ContractorProfileDto>> searchContractors(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry,
            Pageable pageable) {
        
        Page<ContractorProfileDto> contractors = contractorProfileService.searchContractors(
            query, location, industry, pageable);
        return ResponseEntity.ok(contractors);
    }
    
    // Ofertas de trabajo del contratante
    @PostMapping("/job-offers")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<JobOfferDto> createJobOffer(
            @Valid @RequestBody CreateJobOfferRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        ContractorProfileDto profile = contractorProfileService.getProfileByUserId(userId);
        JobOfferDto jobOffer = jobOfferService.createJobOffer(profile.getId(), request);
        return ResponseEntity.ok(jobOffer);
    }
    
    @GetMapping("/job-offers")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<Page<JobOfferDto>> getMyJobOffers(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        UUID userId = getUserIdFromToken(token);
        ContractorProfileDto profile = contractorProfileService.getProfileByUserId(userId);
        Page<JobOfferDto> jobOffers = jobOfferService.getJobOffersByContractor(profile.getId(), pageable);
        return ResponseEntity.ok(jobOffers);
    }
    
    @GetMapping("/{contractorId}/job-offers")
    public ResponseEntity<Page<JobOfferDto>> getContractorJobOffers(
            @PathVariable UUID contractorId,
            Pageable pageable) {
        
        Page<JobOfferDto> jobOffers = jobOfferService.getJobOffersByContractor(contractorId, pageable);
        return ResponseEntity.ok(jobOffers);
    }
    
    @PutMapping("/job-offers/{jobOfferId}")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<JobOfferDto> updateJobOffer(
            @PathVariable UUID jobOfferId,
            @Valid @RequestBody CreateJobOfferRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        ContractorProfileDto profile = contractorProfileService.getProfileByUserId(userId);
        JobOfferDto jobOffer = jobOfferService.updateJobOffer(jobOfferId, profile.getId(), request);
        return ResponseEntity.ok(jobOffer);
    }
    
    @DeleteMapping("/job-offers/{jobOfferId}")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<Void> deleteJobOffer(
            @PathVariable UUID jobOfferId,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        ContractorProfileDto profile = contractorProfileService.getProfileByUserId(userId);
        jobOfferService.deleteJobOffer(jobOfferId, profile.getId());
        return ResponseEntity.ok().build();
    }
    
    private UUID getUserIdFromToken(String token) {
        // Implementar extracción del userId del token JWT
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        // Aquí necesitarías un método para obtener el userId por email
        return UUID.randomUUID(); // Placeholder
    }
}