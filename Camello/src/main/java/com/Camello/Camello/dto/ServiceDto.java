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
public class ServiceDto {
    private UUID id;
    private UUID freelancerId;
    private String freelancerName;
    private String freelancerProfilePicture;
    private UUID categoryId;
    private String categoryName;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer deliveryTime;
    private Integer revisionsIncluded;
    private List<String> images;
    private List<String> tags;
    private Boolean isActive;
    private Boolean isFeatured;
    private Integer viewsCount;
    private Integer ordersCount;
    private BigDecimal rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}