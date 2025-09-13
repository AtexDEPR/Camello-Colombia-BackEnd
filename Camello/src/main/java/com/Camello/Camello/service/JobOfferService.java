package com.Camello.Camello.service;

import com.Camello.Camello.dto.CreateJobOfferRequest;
import com.Camello.Camello.dto.JobOfferDto;
import com.Camello.Camello.entity.ContractorProfile;
import com.Camello.Camello.entity.Category;
import com.Camello.Camello.entity.JobOffer;
import com.Camello.Camello.repository.JobOfferRepository;
import com.Camello.Camello.repository.ContractorProfileRepository;
import com.Camello.Camello.repository.CategoryRepository;
import com.Camello.Camello.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class JobOfferService {
    
    private final JobOfferRepository jobOfferRepository;
    private final ContractorProfileRepository contractorProfileRepository;
    private final CategoryRepository categoryRepository;
    
    public JobOfferDto createJobOffer(UUID contractorId, CreateJobOfferRequest request) {
        ContractorProfile contractor = contractorProfileRepository.findById(contractorId)
            .orElseThrow(() -> new ResourceNotFoundException("Contratante no encontrado"));
        
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        
        JobOffer jobOffer = new JobOffer();
        jobOffer.setContractor(contractor);
        jobOffer.setCategory(category);
        jobOffer.setTitle(request.getTitle());
        jobOffer.setDescription(request.getDescription());
        jobOffer.setBudgetMin(request.getBudgetMin());
        jobOffer.setBudgetMax(request.getBudgetMax());
        jobOffer.setDeadline(request.getDeadline());
        jobOffer.setRequiredSkills(request.getRequiredSkills());
        jobOffer.setExperienceLevel(JobOffer.ExperienceLevel.valueOf(request.getExperienceLevel()));
        jobOffer.setProjectType(JobOffer.ProjectType.valueOf(request.getProjectType()));
        jobOffer.setIsActive(true);
        jobOffer.setApplicationsCount(0);
        
        jobOffer = jobOfferRepository.save(jobOffer);
        return convertToDto(jobOffer);
    }
    
    @Transactional(readOnly = true)
    public Page<JobOfferDto> getAllActiveJobOffers(Pageable pageable) {
        return jobOfferRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable)
            .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<JobOfferDto> getJobOffersByContractor(UUID contractorId, Pageable pageable) {
        ContractorProfile contractor = contractorProfileRepository.findById(contractorId)
            .orElseThrow(() -> new ResourceNotFoundException("Contratante no encontrado"));
        
        return jobOfferRepository.findByContractorOrderByCreatedAtDesc(contractor, pageable)
            .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<JobOfferDto> getJobOffersByCategory(UUID categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        
        return jobOfferRepository.findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(category, pageable)
            .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public JobOfferDto getJobOfferById(UUID jobOfferId) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta de trabajo no encontrada"));
        
        return convertToDto(jobOffer);
    }
    
    public JobOfferDto updateJobOffer(UUID jobOfferId, UUID contractorId, CreateJobOfferRequest request) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta de trabajo no encontrada"));
        
        if (!jobOffer.getContractor().getId().equals(contractorId)) {
            throw new IllegalArgumentException("No tienes permisos para editar esta oferta");
        }
        
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        
        jobOffer.setCategory(category);
        jobOffer.setTitle(request.getTitle());
        jobOffer.setDescription(request.getDescription());
        jobOffer.setBudgetMin(request.getBudgetMin());
        jobOffer.setBudgetMax(request.getBudgetMax());
        jobOffer.setDeadline(request.getDeadline());
        jobOffer.setRequiredSkills(request.getRequiredSkills());
        jobOffer.setExperienceLevel(JobOffer.ExperienceLevel.valueOf(request.getExperienceLevel()));
        jobOffer.setProjectType(JobOffer.ProjectType.valueOf(request.getProjectType()));
        
        jobOffer = jobOfferRepository.save(jobOffer);
        return convertToDto(jobOffer);
    }
    
    public void deleteJobOffer(UUID jobOfferId, UUID contractorId) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta de trabajo no encontrada"));
        
        if (!jobOffer.getContractor().getId().equals(contractorId)) {
            throw new IllegalArgumentException("No tienes permisos para eliminar esta oferta");
        }
        
        jobOffer.setIsActive(false);
        jobOfferRepository.save(jobOffer);
    }
    
    @Transactional(readOnly = true)
    public Page<JobOfferDto> searchJobOffers(String query, UUID categoryId, BigDecimal minBudget, 
                                           BigDecimal maxBudget, String experienceLevel, 
                                           LocalDate deadline, Pageable pageable) {
        
        Page<JobOffer> jobOffers;
        
        if (query != null && !query.trim().isEmpty()) {
            if (categoryId != null) {
                Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
                jobOffers = jobOfferRepository.findByCategoryAndTitleContainingIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(
                    category, query, pageable);
            } else {
                jobOffers = jobOfferRepository.findByTitleContainingIgnoreCaseAndIsActiveTrueOrderByCreatedAtDesc(
                    query, pageable);
            }
        } else {
            jobOffers = jobOfferRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);
        }
        
        return jobOffers.map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<JobOfferDto> getFeaturedJobOffers(Pageable pageable) {
        return jobOfferRepository.findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedAtDesc(pageable)
            .map(this::convertToDto);
    }
    
    public void incrementApplicationsCount(UUID jobOfferId) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta de trabajo no encontrada"));
        
        jobOffer.setApplicationsCount(jobOffer.getApplicationsCount() + 1);
        jobOfferRepository.save(jobOffer);
    }
    
    private JobOfferDto convertToDto(JobOffer jobOffer) {
        JobOfferDto dto = new JobOfferDto();
        dto.setId(jobOffer.getId());
        dto.setContractorId(jobOffer.getContractor().getId());
        dto.setContractorName(jobOffer.getContractor().getContactName());
        dto.setContractorProfilePicture(jobOffer.getContractor().getProfilePicture());
        dto.setCategoryId(jobOffer.getCategory().getId());
        dto.setCategoryName(jobOffer.getCategory().getName());
        dto.setTitle(jobOffer.getTitle());
        dto.setDescription(jobOffer.getDescription());
        dto.setBudgetMin(jobOffer.getBudgetMin());
        dto.setBudgetMax(jobOffer.getBudgetMax());
        dto.setDeadline(jobOffer.getDeadline());
        dto.setRequiredSkills(jobOffer.getRequiredSkills());
        dto.setExperienceLevel(jobOffer.getExperienceLevel().name());
        dto.setProjectType(jobOffer.getProjectType().name());
        dto.setIsActive(jobOffer.getIsActive());
        dto.setIsFeatured(jobOffer.getIsFeatured());
        dto.setApplicationsCount(jobOffer.getApplicationsCount());
        dto.setCreatedAt(jobOffer.getCreatedAt());
        dto.setUpdatedAt(jobOffer.getUpdatedAt());
        return dto;
    }
}