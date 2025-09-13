package com.Camello.Camello.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFreelancerProfileRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;
    
    private String profilePicture;
    
    @Size(min = 10, max = 100, message = "El título debe tener entre 10 y 100 caracteres")
    private String title;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @Size(min = 3, max = 100, message = "La ubicación debe tener entre 3 y 100 caracteres")
    private String location;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Formato de teléfono inválido")
    private String phone;
    
    @Size(max = 20, message = "Máximo 20 habilidades")
    private List<String> skills;
    
    @Size(max = 10, message = "Máximo 10 URLs de portafolio")
    private List<String> portfolioUrls;
    
    @DecimalMin(value = "5000.0", message = "La tarifa mínima es $5,000 COP por hora")
    @DecimalMax(value = "500000.0", message = "La tarifa máxima es $500,000 COP por hora")
    private BigDecimal hourlyRate;
    
    @Pattern(regexp = "AVAILABLE|BUSY|UNAVAILABLE", message = "Estado de disponibilidad inválido")
    private String availability;
}