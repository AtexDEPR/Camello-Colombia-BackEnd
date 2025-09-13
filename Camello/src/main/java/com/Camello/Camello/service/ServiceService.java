package com.Camello.Camello.service;

import com.Camello.Camello.dto.CreateServiceRequest;
import com.Camello.Camello.dto.ServiceDto;
import com.Camello.Camello.entity.FreelancerProfile;
import com.Camello.Camello.entity.Category;
import com.Camello.Camello.repository.ServiceRepository;
import com.Camello.Camello.repository.FreelancerProfileRepository;
import com.Camello.Camello.repository.CategoryRepository;
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
public class ServiceService {
    
    private final ServiceRepository serviceRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final CategoryRepository categoryRepository;
    
    public ServiceDto createService(UUID freelancerId, CreateServiceRequest request) {
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado"));
        
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        
        com.Camello.Camello.entity.Service service = new com.Camello.Camello.entity.Service();
        service.setFreelancer(freelancer);
        service.setCategory(category);
        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDeliveryTime(request.getDeliveryTime());
        service.setRevisionsIncluded(request.getRevisionsIncluded());
        service.setImages(request.getImages());
        service.setTags(request.getTags());
        service.setIsActive(true);
        service.setViewsCount(0);
        service.setOrdersCount(0);
        service.setRating(BigDecimal.ZERO);
        
        service = serviceRepository.save(service);
        return convertToDto(service);
    }
    
    @Transactional(readOnly = true)
    public Page<ServiceDto> getAllServices(Pageable pageable) {
        return serviceRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable)
            .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<ServiceDto> getServicesByFreelancer(UUID freelancerId, Pageable pageable) {
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado"));
        
        return serviceRepository.findByFreelancerOrderByCreatedAtDesc(freelancer, pageable)
            .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<ServiceDto> getServicesByCategory(UUID categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        
        return serviceRepository.findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(category, pageable)
            .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public ServiceDto getServiceById(UUID serviceId) {
        com.Camello.Camello.entity.Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        // Incrementar contador de vistas
        service.setViewsCount(service.getViewsCount() + 1);
        serviceRepository.save(service);
        
        return convertToDto(service);
    }
    
    public ServiceDto updateService(UUID serviceId, UUID freelancerId, CreateServiceRequest request) {
        com.Camello.Camello.entity.Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        if (!service.getFreelancer().getId().equals(freelancerId)) {
            throw new IllegalArgumentException("No tienes permisos para editar este servicio");
        }
        
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        
        service.setCategory(category);
        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDeliveryTime(request.getDeliveryTime());
        service.setRevisionsIncluded(request.getRevisionsIncluded());
        service.setImages(request.getImages());
        service.setTags(request.getTags());
        
        service = serviceRepository.save(service);
        return convertToDto(service);
    }
    
    public void deleteService(UUID serviceId, UUID freelancerId) {
        com.Camello.Camello.entity.Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        if (!service.getFreelancer().getId().equals(freelancerId)) {
            throw new IllegalArgumentException("No tienes permisos para eliminar este servicio");
        }
        
        service.setIsActive(false);
        serviceRepository.save(service);
    }
    
    @Transactional(readOnly = true)
    public Page<ServiceDto> searchServices(String query, UUID categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        if (categoryId != null && minPrice != null && maxPrice != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            return serviceRepository.findByCategoryAndTitleContainingIgnoreCaseAndPriceBetweenAndIsActiveTrueOrderByCreatedAtDesc(
                category, query, minPrice, maxPrice, pageable).map(this::convertToDto);
        } else if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            return serviceRepository.findByCategoryAndTitleContainingIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(
                category, query, pageable).map(this::convertToDto);
        } else if (minPrice != null && maxPrice != null) {
            return serviceRepository.findByTitleContainingIgnoreCaseAndPriceBetweenAndIsActiveTrueOrderByCreatedAtDesc(
                query, minPrice, maxPrice, pageable).map(this::convertToDto);
        } else {
            return serviceRepository.findByTitleContainingIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(
                query, pageable).map(this::convertToDto);
        }
    }
    
    @Transactional(readOnly = true)
    public Page<ServiceDto> getFeaturedServices(Pageable pageable) {
        return serviceRepository.findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedAtDesc(pageable)
            .map(this::convertToDto);
    }
    
    private ServiceDto convertToDto(com.Camello.Camello.entity.Service service) {
        ServiceDto dto = new ServiceDto();
        dto.setId(service.getId());
        dto.setFreelancerId(service.getFreelancer().getId());
        dto.setFreelancerName(service.getFreelancer().getFirstName() + " " + service.getFreelancer().getLastName());
        dto.setFreelancerProfilePicture(service.getFreelancer().getProfilePicture());
        dto.setCategoryId(service.getCategory().getId());
        dto.setCategoryName(service.getCategory().getName());
        dto.setTitle(service.getTitle());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setDeliveryTime(service.getDeliveryTime());
        dto.setRevisionsIncluded(service.getRevisionsIncluded());
        dto.setImages(service.getImages());
        dto.setTags(service.getTags());
        dto.setIsActive(service.getIsActive());
        dto.setIsFeatured(service.getIsFeatured());
        dto.setViewsCount(service.getViewsCount());
        dto.setOrdersCount(service.getOrdersCount());
        dto.setRating(service.getRating());
        dto.setCreatedAt(service.getCreatedAt());
        dto.setUpdatedAt(service.getUpdatedAt());
        return dto;
    }
} 