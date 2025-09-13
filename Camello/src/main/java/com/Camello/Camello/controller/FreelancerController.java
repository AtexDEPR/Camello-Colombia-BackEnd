package com.Camello.Camello.controller;

import com.Camello.Camello.dto.FreelancerProfileDto;
import com.Camello.Camello.dto.UpdateFreelancerProfileRequest;
import com.Camello.Camello.dto.ServiceDto;
import com.Camello.Camello.dto.CreateServiceRequest;
import com.Camello.Camello.service.FreelancerProfileService;
import com.Camello.Camello.service.ServiceService;
import com.Camello.Camello.service.UserContextService;
import com.Camello.Camello.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/freelancers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FreelancerController {
    
    private final FreelancerProfileService freelancerProfileService;
    private final ServiceService serviceService;
    private final UserContextService userContextService;
    
    @PostMapping("/profile")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> createProfile(
            @Valid @RequestBody UpdateFreelancerProfileRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        FreelancerProfileDto profile = freelancerProfileService.createProfile(userId, request);
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> getMyProfile(@RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        FreelancerProfileDto profile = freelancerProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<FreelancerProfileDto> getProfile(@PathVariable UUID profileId) {
        FreelancerProfileDto profile = freelancerProfileService.getProfileById(profileId);
        return ResponseEntity.ok(profile);
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> updateProfile(
            @Valid @RequestBody UpdateFreelancerProfileRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        FreelancerProfileDto profile = freelancerProfileService.updateProfile(userId, request);
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<FreelancerProfileDto>> searchFreelancers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minRate,
            @RequestParam(required = false) BigDecimal maxRate,
            @RequestParam(required = false) String availability,
            Pageable pageable) {
        
        Page<FreelancerProfileDto> freelancers = freelancerProfileService.searchFreelancers(
            query, location, minRate, maxRate, availability, pageable);
        return ResponseEntity.ok(freelancers);
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<Page<FreelancerProfileDto>> getTopRatedFreelancers(Pageable pageable) {
        Page<FreelancerProfileDto> freelancers = freelancerProfileService.getTopRatedFreelancers(pageable);
        return ResponseEntity.ok(freelancers);
    }
    
    // Servicios del freelancer
    @PostMapping("/services")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServiceDto> createService(
            @Valid @RequestBody CreateServiceRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        FreelancerProfileDto profile = freelancerProfileService.getProfileByUserId(userId);
        ServiceDto service = serviceService.createService(profile.getId(), request);
        return ResponseEntity.ok(service);
    }
    
    @GetMapping("/services")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Page<ServiceDto>> getMyServices(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        UUID userId = getUserIdFromToken(token);
        FreelancerProfileDto profile = freelancerProfileService.getProfileByUserId(userId);
        Page<ServiceDto> services = serviceService.getServicesByFreelancer(profile.getId(), pageable);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/{freelancerId}/services")
    public ResponseEntity<Page<ServiceDto>> getFreelancerServices(
            @PathVariable UUID freelancerId,
            Pageable pageable) {
        
        Page<ServiceDto> services = serviceService.getServicesByFreelancer(freelancerId, pageable);
        return ResponseEntity.ok(services);
    }
    
    @PutMapping("/services/{serviceId}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServiceDto> updateService(
            @PathVariable UUID serviceId,
            @Valid @RequestBody CreateServiceRequest request,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        FreelancerProfileDto profile = freelancerProfileService.getProfileByUserId(userId);
        ServiceDto service = serviceService.updateService(serviceId, profile.getId(), request);
        return ResponseEntity.ok(service);
    }
    
    @DeleteMapping("/services/{serviceId}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Void> deleteService(
            @PathVariable UUID serviceId,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = getUserIdFromToken(token);
        FreelancerProfileDto profile = freelancerProfileService.getProfileByUserId(userId);
        serviceService.deleteService(serviceId, profile.getId());
        return ResponseEntity.ok().build();
    }
    
    private UUID getUserIdFromToken(String token) {
        return userContextService.getUserIdFromToken(token);
    }
}