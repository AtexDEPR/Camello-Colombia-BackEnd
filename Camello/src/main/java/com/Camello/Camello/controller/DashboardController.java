package com.Camello.Camello.controller;

import com.Camello.Camello.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {
    
    private final UserContextService userContextService;
    private final FreelancerProfileService freelancerProfileService;
    private final ContractorProfileService contractorProfileService;
    private final ServiceService serviceService;
    private final JobOfferService jobOfferService;
    private final ContractService contractService;
    private final NotificationService notificationService;
    
    @GetMapping("/freelancer")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Map<String, Object>> getFreelancerDashboard(@RequestHeader("Authorization") String token) {
        UUID userId = userContextService.getUserIdFromToken(token);
        
        try {
            var freelancerProfile = freelancerProfileService.getProfileByUserId(userId);
            
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("profile", freelancerProfile);
            
            // Estadísticas básicas
            var services = serviceService.getServicesByFreelancer(freelancerProfile.getId(), 
                org.springframework.data.domain.PageRequest.of(0, 5));
            dashboard.put("totalServices", services.getTotalElements());
            dashboard.put("recentServices", services.getContent());
            
            var contracts = contractService.getContractsByFreelancer(freelancerProfile.getId(),
                org.springframework.data.domain.PageRequest.of(0, 5));
            dashboard.put("totalContracts", contracts.getTotalElements());
            dashboard.put("recentContracts", contracts.getContent());
            
            // Notificaciones no leídas
            Long unreadNotifications = notificationService.getUnreadNotificationsCount(userId);
            dashboard.put("unreadNotifications", unreadNotifications);
            
            return ResponseEntity.ok(dashboard);
            
        } catch (Exception e) {
            // Si no tiene perfil, devolver datos básicos
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("hasProfile", false);
            dashboard.put("unreadNotifications", notificationService.getUnreadNotificationsCount(userId));
            return ResponseEntity.ok(dashboard);
        }
    }
    
    @GetMapping("/contractor")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<Map<String, Object>> getContractorDashboard(@RequestHeader("Authorization") String token) {
        UUID userId = userContextService.getUserIdFromToken(token);
        
        try {
            var contractorProfile = contractorProfileService.getProfileByUserId(userId);
            
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("profile", contractorProfile);
            
            // Estadísticas básicas
            var jobOffers = jobOfferService.getJobOffersByContractor(contractorProfile.getId(),
                org.springframework.data.domain.PageRequest.of(0, 5));
            dashboard.put("totalJobOffers", jobOffers.getTotalElements());
            dashboard.put("recentJobOffers", jobOffers.getContent());
            
            var contracts = contractService.getContractsByContractor(contractorProfile.getId(),
                org.springframework.data.domain.PageRequest.of(0, 5));
            dashboard.put("totalContracts", contracts.getTotalElements());
            dashboard.put("recentContracts", contracts.getContent());
            
            // Notificaciones no leídas
            Long unreadNotifications = notificationService.getUnreadNotificationsCount(userId);
            dashboard.put("unreadNotifications", unreadNotifications);
            
            return ResponseEntity.ok(dashboard);
            
        } catch (Exception e) {
            // Si no tiene perfil, devolver datos básicos
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("hasProfile", false);
            dashboard.put("unreadNotifications", notificationService.getUnreadNotificationsCount(userId));
            return ResponseEntity.ok(dashboard);
        }
    }
}