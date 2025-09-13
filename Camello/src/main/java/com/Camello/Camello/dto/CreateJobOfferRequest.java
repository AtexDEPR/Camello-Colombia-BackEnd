package com.Camello.Camello.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobOfferRequest {
    
    @NotNull(message = "La categoría es obligatoria")
    private UUID categoryId;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 10, max = 100, message = "El título debe tener entre 10 y 100 caracteres")
    private String title;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 50, max = 2000, message = "La descripción debe tener entre 50 y 2000 caracteres")
    private String description;
    
    @DecimalMin(value = "10000.0", message = "El presupuesto mínimo es $10,000 COP")
    private BigDecimal budgetMin;
    
    @DecimalMax(value = "50000000.0", message = "El presupuesto máximo es $50,000,000 COP")
    private BigDecimal budgetMax;
    
    @Future(message = "La fecha límite debe ser futura")
    private LocalDate deadline;
    
    @Size(min = 1, max = 15, message = "Debe especificar entre 1 y 15 habilidades requeridas")
    private List<String> requiredSkills;
    
    @Pattern(regexp = "BEGINNER|INTERMEDIATE|EXPERT", message = "Nivel de experiencia inválido")
    private String experienceLevel = "INTERMEDIATE";
    
    @Pattern(regexp = "ONE_TIME|ONGOING", message = "Tipo de proyecto inválido")
    private String projectType = "ONE_TIME";
}