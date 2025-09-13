package com.Camello.Camello.service;

import com.Camello.Camello.entity.JobApplication;
import com.Camello.Camello.entity.JobOffer;
import com.Camello.Camello.entity.FreelancerProfile;
import com.Camello.Camello.repository.JobApplicationRepository;
import com.Camello.Camello.repository.JobOfferRepository;
import com.Camello.Camello.repository.FreelancerProfileRepository;
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
public class JobApplicationService {
    
    private final JobApplicationRepository jobApplicationRepository;
    private final JobOfferRepository jobOfferRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final JobOfferService jobOfferService;
    private final NotificationService notificationService;
    
    public JobApplication applyToJob(UUID jobOfferId, UUID freelancerId, String coverLetter, 
                                   BigDecimal proposedPrice, Integer estimatedDeliveryDays) {
        
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta de trabajo no encontrada"));
        
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado"));
        
        // Verificar si ya aplicó
        if (jobApplicationRepository.existsByJobOfferAndFreelancer(jobOffer, freelancer)) {
            throw new IllegalArgumentException("Ya has aplicado a esta oferta de trabajo");
        }
        
        JobApplication application = new JobApplication();
        application.setJobOffer(jobOffer);
        application.setFreelancer(freelancer);
        application.setCoverLetter(coverLetter);
        application.setProposedPrice(proposedPrice);
        application.setEstimatedDeliveryDays(estimatedDeliveryDays);
        application.setStatus(JobApplication.ApplicationStatus.PENDING);
        
        application = jobApplicationRepository.save(application);
        
        // Incrementar contador de aplicaciones
        jobOfferService.incrementApplicationsCount(jobOfferId);
        
        // Enviar notificación al contratante
        notificationService.createNotification(
            jobOffer.getContractor().getUser().getId(),
            "NEW_APPLICATION",
            "Nueva aplicación recibida",
            "Has recibido una nueva aplicación para tu oferta: " + jobOffer.getTitle(),
            null
        );
        
        return application;
    }
    
    @Transactional(readOnly = true)
    public Page<JobApplication> getApplicationsByJobOffer(UUID jobOfferId, Pageable pageable) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta de trabajo no encontrada"));
        
        return jobApplicationRepository.findByJobOfferOrderByCreatedAtDesc(jobOffer, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<JobApplication> getApplicationsByFreelancer(UUID freelancerId, Pageable pageable) {
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado"));
        
        return jobApplicationRepository.findByFreelancerOrderByCreatedAtDesc(freelancer, pageable);
    }
    
    public JobApplication updateApplicationStatus(UUID applicationId, UUID contractorId, 
                                                JobApplication.ApplicationStatus status) {
        
        JobApplication application = jobApplicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada"));
        
        // Verificar que el contratante sea el dueño de la oferta
        if (!application.getJobOffer().getContractor().getId().equals(contractorId)) {
            throw new IllegalArgumentException("No tienes permisos para modificar esta aplicación");
        }
        
        application.setStatus(status);
        application = jobApplicationRepository.save(application);
        
        // Enviar notificación al freelancer
        String message = status == JobApplication.ApplicationStatus.ACCEPTED 
            ? "Tu aplicación ha sido aceptada para: " + application.getJobOffer().getTitle()
            : "Tu aplicación ha sido rechazada para: " + application.getJobOffer().getTitle();
            
        notificationService.createNotification(
            application.getFreelancer().getUser().getId(),
            "APPLICATION_" + status.name(),
            "Estado de aplicación actualizado",
            message,
            null
        );
        
        return application;
    }
    
    public void withdrawApplication(UUID applicationId, UUID freelancerId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada"));
        
        if (!application.getFreelancer().getId().equals(freelancerId)) {
            throw new IllegalArgumentException("No tienes permisos para retirar esta aplicación");
        }
        
        if (application.getStatus() != JobApplication.ApplicationStatus.PENDING) {
            throw new IllegalArgumentException("Solo puedes retirar aplicaciones pendientes");
        }
        
        application.setStatus(JobApplication.ApplicationStatus.WITHDRAWN);
        jobApplicationRepository.save(application);
    }
}