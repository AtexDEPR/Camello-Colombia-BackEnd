package com.Camello.Camello.service;

import com.Camello.Camello.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceService {
    
    private final ServiceRepository serviceRepository;
    
    public Page<com.Camello.Camello.entity.Service> getAllActiveServices(Pageable pageable) {
        return serviceRepository.findAllActive(pageable);
    }
    
    public Optional<com.Camello.Camello.entity.Service> getServiceById(UUID id) {
        return serviceRepository.findById(id);
    }
    
    public Page<com.Camello.Camello.entity.Service> getServicesByFreelancer(UUID freelancerId, Pageable pageable) {
        return serviceRepository.findByFreelancerId(freelancerId, pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> getServicesByCategory(UUID categoryId, Pageable pageable) {
        return serviceRepository.findByCategoryId(categoryId, pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> getServicesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return serviceRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> getServicesByMinRating(BigDecimal minRating, Pageable pageable) {
        return serviceRepository.findByMinRating(minRating, pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> getFeaturedServices(Pageable pageable) {
        return serviceRepository.findFeatured(pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> getTopRatedServices(Pageable pageable) {
        return serviceRepository.findTopRated(pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> getMostOrderedServices(Pageable pageable) {
        return serviceRepository.findMostOrdered(pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> getMostViewedServices(Pageable pageable) {
        return serviceRepository.findMostViewed(pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> getServicesByTag(String tag, Pageable pageable) {
        return serviceRepository.findByTag(tag, pageable);
    }
    
    public Page<com.Camello.Camello.entity.Service> searchServices(String searchTerm, Pageable pageable) {
        return serviceRepository.searchByTitleOrDescription(searchTerm, pageable);
    }
    
    public com.Camello.Camello.entity.Service createService(com.Camello.Camello.entity.Service service) {
        service.setIsActive(true);
        service.setViewsCount(0);
        service.setOrdersCount(0);
        service.setRating(BigDecimal.ZERO);
        return serviceRepository.save(service);
    }
    
    public com.Camello.Camello.entity.Service updateService(UUID id, com.Camello.Camello.entity.Service serviceDetails) {
        com.Camello.Camello.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        
        service.setTitle(serviceDetails.getTitle());
        service.setDescription(serviceDetails.getDescription());
        service.setPrice(serviceDetails.getPrice());
        service.setDeliveryTime(serviceDetails.getDeliveryTime());
        service.setRevisionsIncluded(serviceDetails.getRevisionsIncluded());
        service.setImages(serviceDetails.getImages());
        service.setTags(serviceDetails.getTags());
        
        return serviceRepository.save(service);
    }
    
    public void deactivateService(UUID id) {
        com.Camello.Camello.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        
        service.setIsActive(false);
        serviceRepository.save(service);
    }
    
    public void deleteService(UUID id) {
        serviceRepository.deleteById(id);
    }
    
    public void incrementViews(UUID id) {
        serviceRepository.findById(id).ifPresent(service -> {
            service.setViewsCount(service.getViewsCount() + 1);
            serviceRepository.save(service);
        });
    }
} 