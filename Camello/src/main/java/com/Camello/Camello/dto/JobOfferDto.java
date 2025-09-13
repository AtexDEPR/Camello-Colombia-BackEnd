package com.Camello.Camello.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferDto {
    private UUID id;
    private UUID contractorId;
    private String contractorName;
    private String contractorProfilePicture;
    private UUID categoryId;
    private String categoryName;
    private String title;
    private String description;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private LocalDate deadline;
    private List<String> requiredSkills;
    private String experienceLevel;
    private String projectType;
    private Boolean isActive;
    private Boolean isFeatured;
    private Integer applicationsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}