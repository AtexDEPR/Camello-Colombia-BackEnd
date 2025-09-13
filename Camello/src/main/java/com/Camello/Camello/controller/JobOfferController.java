package com.Camello.Camello.controller;

import com.Camello.Camello.dto.JobOfferDto;
import com.Camello.Camello.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-offers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobOfferController {
    
    private final JobOfferService jobOfferService;
    
    @GetMapping
    public ResponseEntity<Page<JobOfferDto>> getAllJobOffers(Pageable pageable) {
        Page<JobOfferDto> jobOffers = jobOfferService.getAllActiveJobOffers(pageable);
        return ResponseEntity.ok(jobOffers);
    }
    
    @GetMapping("/{jobOfferId}")
    public ResponseEntity<JobOfferDto> getJobOffer(@PathVariable UUID jobOfferId) {
        JobOfferDto jobOffer = jobOfferService.getJobOfferById(jobOfferId);
        return ResponseEntity.ok(jobOffer);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<JobOfferDto>> getJobOffersByCategory(
            @PathVariable UUID categoryId,
            Pageable pageable) {
        
        Page<JobOfferDto> jobOffers = jobOfferService.getJobOffersByCategory(categoryId, pageable);
        return ResponseEntity.ok(jobOffers);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<JobOfferDto>> searchJobOffers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minBudget,
            @RequestParam(required = false) BigDecimal maxBudget,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) LocalDate deadline,
            Pageable pageable) {
        
        Page<JobOfferDto> jobOffers = jobOfferService.searchJobOffers(
            query, categoryId, minBudget, maxBudget, experienceLevel, deadline, pageable);
        return ResponseEntity.ok(jobOffers);
    }
    
    @GetMapping("/featured")
    public ResponseEntity<Page<JobOfferDto>> getFeaturedJobOffers(Pageable pageable) {
        Page<JobOfferDto> jobOffers = jobOfferService.getFeaturedJobOffers(pageable);
        return ResponseEntity.ok(jobOffers);
    }
}