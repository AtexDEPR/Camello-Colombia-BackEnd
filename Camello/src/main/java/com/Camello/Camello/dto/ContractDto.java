package com.Camello.Camello.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {
    private UUID id;
    private UUID serviceId;
    private String serviceTitle;
    private UUID jobOfferId;
    private String jobOfferTitle;
    private UUID freelancerId;
    private String freelancerName;
    private UUID contractorId;
    private String contractorName;
    private String title;
    private String description;
    private BigDecimal agreedPrice;
    private LocalDate deliveryDate;
    private String status;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}