package com.Camello.Camello.repository;

import com.Camello.Camello.entity.ContractorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractorProfileRepository extends JpaRepository<ContractorProfile, UUID> {
    
    Optional<ContractorProfile> findByUserId(UUID userId);
    
    @Query("SELECT cp FROM ContractorProfile cp WHERE cp.user.isActive = true")
    Page<ContractorProfile> findAllActive(Pageable pageable);
    
    @Query("SELECT cp FROM ContractorProfile cp WHERE cp.location = :location AND cp.user.isActive = true")
    Page<ContractorProfile> findByLocation(@Param("location") String location, Pageable pageable);
    
    @Query("SELECT cp FROM ContractorProfile cp WHERE cp.rating >= :minRating AND cp.user.isActive = true")
    Page<ContractorProfile> findByMinRating(@Param("minRating") BigDecimal minRating, Pageable pageable);
    
    @Query("SELECT cp FROM ContractorProfile cp WHERE cp.industry = :industry AND cp.user.isActive = true")
    Page<ContractorProfile> findByIndustry(@Param("industry") String industry, Pageable pageable);
    
    @Query("SELECT cp FROM ContractorProfile cp WHERE cp.companySize = :companySize AND cp.user.isActive = true")
    Page<ContractorProfile> findByCompanySize(@Param("companySize") ContractorProfile.CompanySize companySize, Pageable pageable);
} 