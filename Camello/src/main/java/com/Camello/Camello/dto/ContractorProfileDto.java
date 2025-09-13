package com.Camello.Camello.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractorProfileDto {
    private UUID id;
    private UUID userId;
    private String companyName;
    private String contactName;
    private String profilePicture;
    private String description;
    private String location;
    private String phone;
    private String website;
    private String industry;
    private String companySize;
    private BigDecimal rating;
    private Integer totalReviews;
    private BigDecimal totalSpent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}