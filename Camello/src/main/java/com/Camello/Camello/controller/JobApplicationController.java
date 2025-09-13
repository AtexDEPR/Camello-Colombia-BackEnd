package com.Camello.Camello.controller;

import com.Camello.Camello.entity.JobApplication;
import com.Camello.Camello.service.JobApplicationService;
import com.Camello.Camello.service.UserContextService;
import com.Camello.Camello.service.FreelancerProfileService;
import com.Camello.Camello.service.ContractorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobApplicationController {
    
    private final JobApplicationService jobApplicationService;
    private final UserContextService userContextService;
    private final FreelancerProfileService freelancerProfileService;
    private final ContractorProfileService contractorProfileService;
    
    @PostMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<JobApplication> applyToJob(
            @RequestParam UUID jobOfferId,
            @RequestParam String coverLetter,
            @RequestParam(required = false) BigDecimal proposedPrice,
            @RequestParam(required = false) Integer estimatedDeliveryDays,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        var freelancerProfile = freelancerProfileService.getProfileByUserId(userId);
        
        JobApplication application = jobApplicationService.applyToJob(
            jobOfferId, freelancerProfile.getId(), coverLetter, proposedPrice, estimatedDeliveryDays);
        
        return ResponseEntity.ok(application);
    }
    
    @GetMapping("/job-offer/{jobOfferId}")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<Page<JobApplication>> getJobOfferApplications(
            @PathVariable UUID jobOfferId,
            Pageable pageable) {
        
        Page<JobApplication> applications = jobApplicationService.getApplicationsByJobOffer(jobOfferId, pageable);
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/freelancer")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Page<JobApplication>> getFreelancerApplications(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        var freelancerProfile = freelancerProfileService.getProfileByUserId(userId);
        
        Page<JobApplication> applications = jobApplicationService.getApplicationsByFreelancer(
            freelancerProfile.getId(), pageable);
        
        return ResponseEntity.ok(applications);
    }
    
    @PutMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<JobApplication> updateApplicationStatus(
            @PathVariable UUID applicationId,
            @RequestParam JobApplication.ApplicationStatus status,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        var contractorProfile = contractorProfileService.getProfileByUserId(userId);
        
        JobApplication application = jobApplicationService.updateApplicationStatus(
            applicationId, contractorProfile.getId(), status);
        
        return ResponseEntity.ok(application);
    }
    
    @PutMapping("/{applicationId}/withdraw")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Void> withdrawApplication(
            @PathVariable UUID applicationId,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        var freelancerProfile = freelancerProfileService.getProfileByUserId(userId);
        
        jobApplicationService.withdrawApplication(applicationId, freelancerProfile.getId());
        return ResponseEntity.ok().build();
    }
}