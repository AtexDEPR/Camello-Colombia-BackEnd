package com.Camello.Camello.repository;

import com.Camello.Camello.entity.JobApplication;
import com.Camello.Camello.entity.JobOffer;
import com.Camello.Camello.entity.FreelancerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {
    
    Page<JobApplication> findByJobOfferOrderByCreatedAtDesc(JobOffer jobOffer, Pageable pageable);
    
    Page<JobApplication> findByFreelancerOrderByCreatedAtDesc(FreelancerProfile freelancer, Pageable pageable);
    
    Optional<JobApplication> findByJobOfferAndFreelancer(JobOffer jobOffer, FreelancerProfile freelancer);
    
    boolean existsByJobOfferAndFreelancer(JobOffer jobOffer, FreelancerProfile freelancer);
    
    List<JobApplication> findByJobOfferAndStatus(JobOffer jobOffer, JobApplication.ApplicationStatus status);
    
    Long countByJobOffer(JobOffer jobOffer);
}