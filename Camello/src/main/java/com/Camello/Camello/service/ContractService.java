package com.Camello.Camello.service;

import com.Camello.Camello.dto.ContractDto;
import com.Camello.Camello.entity.Contract;
import com.Camello.Camello.entity.FreelancerProfile;
import com.Camello.Camello.entity.ContractorProfile;
import com.Camello.Camello.entity.JobOffer;
import com.Camello.Camello.repository.ContractRepository;
import com.Camello.Camello.repository.FreelancerProfileRepository;
import com.Camello.Camello.repository.ContractorProfileRepository;
import com.Camello.Camello.repository.JobOfferRepository;
import com.Camello.Camello.repository.ServiceRepository;
import com.Camello.Camello.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractService {
    
    private final ContractRepository contractRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final ContractorProfileRepository contractorProfileRepository;
    private final JobOfferRepository jobOfferRepository;
    private final ServiceRepository serviceRepository;
    private final NotificationService notificationService;
    
    public ContractDto createContractFromJobOffer(UUID jobOfferId, UUID freelancerId, UUID contractorId,
                                                 String title, String description, BigDecimal agreedPrice,
                                                 LocalDate deliveryDate) {
        
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta de trabajo no encontrada"));
        
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado"));
        
        ContractorProfile contractor = contractorProfileRepository.findById(contractorId)
            .orElseThrow(() -> new ResourceNotFoundException("Contratante no encontrado"));
        
        Contract contract = new Contract();
        contract.setJobOffer(jobOffer);
        contract.setFreelancer(freelancer);
        contract.setContractor(contractor);
        contract.setTitle(title);
        contract.setDescription(description);
        contract.setAgreedPrice(agreedPrice);
        contract.setDeliveryDate(deliveryDate);
        contract.setStatus(Contract.ContractStatus.ACTIVE);
        contract.setPaymentStatus(Contract.PaymentStatus.PENDING);
        
        contract = contractRepository.save(contract);
        
        // Notificar a ambas partes
        notificationService.createNotification(
            freelancer.getUser().getId(),
            "CONTRACT_CREATED",
            "Nuevo contrato creado",
            "Se ha creado un nuevo contrato: " + title,
            null
        );
        
        notificationService.createNotification(
            contractor.getUser().getId(),
            "CONTRACT_CREATED",
            "Nuevo contrato creado",
            "Se ha creado un nuevo contrato: " + title,
            null
        );
        
        return convertToDto(contract);
    }
    
    public ContractDto createContractFromService(UUID serviceId, UUID contractorId,
                                               String description, BigDecimal agreedPrice,
                                               LocalDate deliveryDate) {
        
        com.Camello.Camello.entity.Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        ContractorProfile contractor = contractorProfileRepository.findById(contractorId)
            .orElseThrow(() -> new ResourceNotFoundException("Contratante no encontrado"));
        
        Contract contract = new Contract();
        contract.setService(service);
        contract.setFreelancer(service.getFreelancer());
        contract.setContractor(contractor);
        contract.setTitle(service.getTitle());
        contract.setDescription(description);
        contract.setAgreedPrice(agreedPrice);
        contract.setDeliveryDate(deliveryDate);
        contract.setStatus(Contract.ContractStatus.ACTIVE);
        contract.setPaymentStatus(Contract.PaymentStatus.PENDING);
        
        contract = contractRepository.save(contract);
        
        // Incrementar contador de órdenes del servicio
        service.setOrdersCount(service.getOrdersCount() + 1);
        serviceRepository.save(service);
        
        // Notificar al freelancer
        notificationService.createNotification(
            service.getFreelancer().getUser().getId(),
            "CONTRACT_CREATED",
            "Nuevo contrato creado",
            "Has recibido una nueva orden para tu servicio: " + service.getTitle(),
            null
        );
        
        return convertToDto(contract);
    }
    
    @Transactional(readOnly = true)
    public Page<ContractDto> getContractsByFreelancer(UUID freelancerId, Pageable pageable) {
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado"));
        
        return contractRepository.findByFreelancerOrderByCreatedAtDesc(freelancer, pageable)
            .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<ContractDto> getContractsByContractor(UUID contractorId, Pageable pageable) {
        ContractorProfile contractor = contractorProfileRepository.findById(contractorId)
            .orElseThrow(() -> new ResourceNotFoundException("Contratante no encontrado"));
        
        return contractRepository.findByContractorOrderByCreatedAtDesc(contractor, pageable)
            .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public ContractDto getContractById(UUID contractId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contrato no encontrado"));
        
        return convertToDto(contract);
    }
    
    public ContractDto updateContractStatus(UUID contractId, UUID userId, Contract.ContractStatus status) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contrato no encontrado"));
        
        // Verificar permisos
        boolean isFreelancer = contract.getFreelancer().getUser().getId().equals(userId);
        boolean isContractor = contract.getContractor().getUser().getId().equals(userId);
        
        if (!isFreelancer && !isContractor) {
            throw new IllegalArgumentException("No tienes permisos para modificar este contrato");
        }
        
        // Solo el freelancer puede marcar como entregado
        if (status == Contract.ContractStatus.DELIVERED && !isFreelancer) {
            throw new IllegalArgumentException("Solo el freelancer puede marcar el contrato como entregado");
        }
        
        // Solo el contratante puede marcar como completado
        if (status == Contract.ContractStatus.COMPLETED && !isContractor) {
            throw new IllegalArgumentException("Solo el contratante puede marcar el contrato como completado");
        }
        
        contract.setStatus(status);
        
        if (status == Contract.ContractStatus.COMPLETED) {
            contract.setCompletedAt(LocalDateTime.now());
            contract.setPaymentStatus(Contract.PaymentStatus.PAID);
        }
        
        contract = contractRepository.save(contract);
        
        // Notificar a la otra parte
        UUID recipientId = isFreelancer ? contract.getContractor().getUser().getId() 
                                       : contract.getFreelancer().getUser().getId();
        
        String message = "El estado del contrato '" + contract.getTitle() + "' ha cambiado a: " + status.name();
        
        notificationService.createNotification(
            recipientId,
            "CONTRACT_STATUS_UPDATED",
            "Estado de contrato actualizado",
            message,
            null
        );
        
        return convertToDto(contract);
    }
    
    public void cancelContract(UUID contractId, UUID userId, String reason) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contrato no encontrado"));
        
        // Verificar permisos
        boolean isFreelancer = contract.getFreelancer().getUser().getId().equals(userId);
        boolean isContractor = contract.getContractor().getUser().getId().equals(userId);
        
        if (!isFreelancer && !isContractor) {
            throw new IllegalArgumentException("No tienes permisos para cancelar este contrato");
        }
        
        if (contract.getStatus() == Contract.ContractStatus.COMPLETED) {
            throw new IllegalArgumentException("No se puede cancelar un contrato completado");
        }
        
        contract.setStatus(Contract.ContractStatus.CANCELLED);
        contract.setPaymentStatus(Contract.PaymentStatus.REFUNDED);
        contractRepository.save(contract);
        
        // Notificar a la otra parte
        UUID recipientId = isFreelancer ? contract.getContractor().getUser().getId() 
                                       : contract.getFreelancer().getUser().getId();
        
        notificationService.createNotification(
            recipientId,
            "CONTRACT_CANCELLED",
            "Contrato cancelado",
            "El contrato '" + contract.getTitle() + "' ha sido cancelado. Razón: " + reason,
            null
        );
    }
    
    private ContractDto convertToDto(Contract contract) {
        ContractDto dto = new ContractDto();
        dto.setId(contract.getId());
        
        if (contract.getService() != null) {
            dto.setServiceId(contract.getService().getId());
            dto.setServiceTitle(contract.getService().getTitle());
        }
        
        if (contract.getJobOffer() != null) {
            dto.setJobOfferId(contract.getJobOffer().getId());
            dto.setJobOfferTitle(contract.getJobOffer().getTitle());
        }
        
        dto.setFreelancerId(contract.getFreelancer().getId());
        dto.setFreelancerName(contract.getFreelancer().getFirstName() + " " + contract.getFreelancer().getLastName());
        dto.setContractorId(contract.getContractor().getId());
        dto.setContractorName(contract.getContractor().getContactName());
        dto.setTitle(contract.getTitle());
        dto.setDescription(contract.getDescription());
        dto.setAgreedPrice(contract.getAgreedPrice());
        dto.setDeliveryDate(contract.getDeliveryDate());
        dto.setStatus(contract.getStatus().name());
        dto.setPaymentStatus(contract.getPaymentStatus().name());
        dto.setCreatedAt(contract.getCreatedAt());
        dto.setUpdatedAt(contract.getUpdatedAt());
        dto.setCompletedAt(contract.getCompletedAt());
        
        return dto;
    }
}