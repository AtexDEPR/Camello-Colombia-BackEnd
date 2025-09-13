package com.Camello.Camello.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Camello.Camello.entity.FreelancerProfile;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, UUID> {
    
    Optional<FreelancerProfile> findByUserId(UUID userId);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE fp.user.isActive = true")
    Page<FreelancerProfile> findAllActive(Pageable pageable);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE fp.location = :location AND fp.user.isActive = true")
    Page<FreelancerProfile> findByLocation(@Param("location") String location, Pageable pageable);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE fp.rating >= :minRating AND fp.user.isActive = true")
    Page<FreelancerProfile> findByMinRating(@Param("minRating") BigDecimal minRating, Pageable pageable);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE fp.hourlyRate BETWEEN :minRate AND :maxRate AND fp.user.isActive = true")
    Page<FreelancerProfile> findByHourlyRateRange(@Param("minRate") BigDecimal minRate, 
                                                  @Param("maxRate") BigDecimal maxRate, 
                                                  Pageable pageable);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE :skill MEMBER OF fp.skills AND fp.user.isActive = true")
    Page<FreelancerProfile> findBySkill(@Param("skill") String skill, Pageable pageable);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE fp.availability = :availability AND fp.user.isActive = true")
    Page<FreelancerProfile> findByAvailability(@Param("availability") FreelancerProfile.Availability availability, 
                                              Pageable pageable);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE fp.user.isActive = true ORDER BY fp.rating DESC")
    Page<FreelancerProfile> findTopRated(Pageable pageable);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE fp.user.isActive = true ORDER BY fp.totalEarnings DESC")
    Page<FreelancerProfile> findTopEarners(Pageable pageable);
} 