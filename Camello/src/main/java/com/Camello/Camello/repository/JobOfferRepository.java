package com.Camello.Camello.repository;

import com.Camello.Camello.entity.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, UUID> {
    
    @Query("SELECT jo FROM JobOffer jo WHERE jo.isActive = true")
    Page<JobOffer> findAllActive(Pageable pageable);
    
    @Query("SELECT jo FROM JobOffer jo WHERE jo.contractor.id = :contractorId AND jo.isActive = true")
    Page<JobOffer> findByContractorId(@Param("contractorId") UUID contractorId, Pageable pageable);
    
    @Query("SELECT jo FROM JobOffer jo WHERE jo.category.id = :categoryId AND jo.isActive = true")
    Page<JobOffer> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);
    
    @Query("SELECT jo FROM JobOffer jo WHERE jo.budgetMax BETWEEN :minBudget AND :maxBudget AND jo.isActive = true")
    Page<JobOffer> findByBudgetRange(@Param("minBudget") BigDecimal minBudget, 
                                     @Param("maxBudget") BigDecimal maxBudget, 
                                     Pageable pageable);
    
    @Query("SELECT jo FROM JobOffer jo WHERE jo.experienceLevel = :experienceLevel AND jo.isActive = true")
    Page<JobOffer> findByExperienceLevel(@Param("experienceLevel") JobOffer.ExperienceLevel experienceLevel, 
                                        Pageable pageable);
    
    @Query("SELECT jo FROM JobOffer jo WHERE jo.projectType = :projectType AND jo.isActive = true")
    Page<JobOffer> findByProjectType(@Param("projectType") JobOffer.ProjectType projectType, 
                                     Pageable pageable);
    
    @Query("SELECT jo FROM JobOffer jo WHERE jo.isFeatured = true AND jo.isActive = true")
    Page<JobOffer> findFeatured(Pageable pageable);
    
    @Query("SELECT jo FROM JobOffer jo WHERE jo.title ILIKE %:searchTerm% OR jo.description ILIKE %:searchTerm% AND jo.isActive = true")
    Page<JobOffer> searchByTitleOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT jo FROM JobOffer jo WHERE :skill MEMBER OF jo.requiredSkills AND jo.isActive = true")
    Page<JobOffer> findByRequiredSkill(@Param("skill") String skill, Pageable pageable);
} 