package com.Camello.Camello.controller;

import com.Camello.Camello.dto.ContractDto;
import com.Camello.Camello.entity.Contract;
import com.Camello.Camello.service.ContractService;
import com.Camello.Camello.service.UserContextService;
import com.Camello.Camello.service.FreelancerProfileService;
import com.Camello.Camello.service.ContractorProfileService;
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
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContractController {
    
    private final ContractService contractService;
    private final UserContextService userContextService;
    private final FreelancerProfileService freelancerProfileService;
    private final ContractorProfileService contractorProfileService;
    
    @PostMapping("/from-job-offer")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<ContractDto> createContractFromJobOffer(
            @RequestParam UUID jobOfferId,
            @RequestParam UUID freelancerId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam BigDecimal agreedPrice,
            @RequestParam LocalDate deliveryDate,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        var contractorProfile = contractorProfileService.getProfileByUserId(userId);
        
        ContractDto contract = contractService.createContractFromJobOffer(
            jobOfferId, freelancerId, contractorProfile.getId(), title, description, agreedPrice, deliveryDate);
        
        return ResponseEntity.ok(contract);
    }
    
    @PostMapping("/from-service")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<ContractDto> createContractFromService(
            @RequestParam UUID serviceId,
            @RequestParam String description,
            @RequestParam BigDecimal agreedPrice,
            @RequestParam LocalDate deliveryDate,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        var contractorProfile = contractorProfileService.getProfileByUserId(userId);
        
        ContractDto contract = contractService.createContractFromService(
            serviceId, contractorProfile.getId(), description, agreedPrice, deliveryDate);
        
        return ResponseEntity.ok(contract);
    }
    
    @GetMapping("/freelancer")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Page<ContractDto>> getFreelancerContracts(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        var freelancerProfile = freelancerProfileService.getProfileByUserId(userId);
        
        Page<ContractDto> contracts = contractService.getContractsByFreelancer(freelancerProfile.getId(), pageable);
        return ResponseEntity.ok(contracts);
    }
    
    @GetMapping("/contractor")
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<Page<ContractDto>> getContractorContracts(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        var contractorProfile = contractorProfileService.getProfileByUserId(userId);
        
        Page<ContractDto> contracts = contractService.getContractsByContractor(contractorProfile.getId(), pageable);
        return ResponseEntity.ok(contracts);
    }
    
    @GetMapping("/{contractId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<ContractDto> getContract(@PathVariable UUID contractId) {
        ContractDto contract = contractService.getContractById(contractId);
        return ResponseEntity.ok(contract);
    }
    
    @PutMapping("/{contractId}/status")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<ContractDto> updateContractStatus(
            @PathVariable UUID contractId,
            @RequestParam Contract.ContractStatus status,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        ContractDto contract = contractService.updateContractStatus(contractId, userId, status);
        return ResponseEntity.ok(contract);
    }
    
    @PutMapping("/{contractId}/cancel")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('CONTRACTOR')")
    public ResponseEntity<Void> cancelContract(
            @PathVariable UUID contractId,
            @RequestParam String reason,
            @RequestHeader("Authorization") String token) {
        
        UUID userId = userContextService.getUserIdFromToken(token);
        contractService.cancelContract(contractId, userId, reason);
        return ResponseEntity.ok().build();
    }
}