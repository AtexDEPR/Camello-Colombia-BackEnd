package com.Camello.Camello.controller;

import com.Camello.Camello.entity.Category;
import com.Camello.Camello.entity.Service;
import com.Camello.Camello.dto.ServiceDto;
import com.Camello.Camello.service.CategoryService;
import com.Camello.Camello.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicController {
    
    private final CategoryService categoryService;
    private final ServiceService serviceService;
    
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategories());
    }
    
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/services")
    public ResponseEntity<Page<ServiceDto>> getServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String searchTerm) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return ResponseEntity.ok(serviceService.searchServices(searchTerm, categoryId, minPrice, maxPrice, pageable));
        }
        
        if (categoryId != null) {
            return ResponseEntity.ok(serviceService.getServicesByCategory(categoryId, pageable));
        }
        
        return ResponseEntity.ok(serviceService.getAllServices(pageable));
    }
    
    @GetMapping("/services/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable UUID id) {
        ServiceDto service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }
    
    @GetMapping("/services/featured")
    public ResponseEntity<Page<ServiceDto>> getFeaturedServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(serviceService.getFeaturedServices(pageable));
    }
} 