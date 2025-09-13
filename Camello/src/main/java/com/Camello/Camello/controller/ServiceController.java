package com.Camello.Camello.controller;

import com.Camello.Camello.dto.ServiceDto;
import com.Camello.Camello.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServiceController {
    
    private final ServiceService serviceService;
    
    @GetMapping
    public ResponseEntity<Page<ServiceDto>> getAllServices(Pageable pageable) {
        Page<ServiceDto> services = serviceService.getAllServices(pageable);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDto> getService(@PathVariable UUID serviceId) {
        ServiceDto service = serviceService.getServiceById(serviceId);
        return ResponseEntity.ok(service);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ServiceDto>> getServicesByCategory(
            @PathVariable UUID categoryId,
            Pageable pageable) {
        
        Page<ServiceDto> services = serviceService.getServicesByCategory(categoryId, pageable);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ServiceDto>> searchServices(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable) {
        
        Page<ServiceDto> services = serviceService.searchServices(query, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/featured")
    public ResponseEntity<Page<ServiceDto>> getFeaturedServices(Pageable pageable) {
        Page<ServiceDto> services = serviceService.getFeaturedServices(pageable);
        return ResponseEntity.ok(services);
    }
}