package com.Camello.Camello.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfileDto {
    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String title;
    private String description;
    private String location;
    private String phone;
    private List<String> skills;
    private List<String> portfolioUrls;
    private BigDecimal hourlyRate;
    private String availability;
    private BigDecimal rating;
    private Integer totalReviews;
    private BigDecimal totalEarnings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}