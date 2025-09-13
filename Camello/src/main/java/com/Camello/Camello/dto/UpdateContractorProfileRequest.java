package com.Camello.Camello.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateContractorProfileRequest {
    
    @Size(min = 2, max = 100, message = "El nombre de la empresa debe tener entre 2 y 100 caracteres")
    private String companyName;
    
    @NotBlank(message = "El nombre de contacto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre de contacto debe tener entre 2 y 100 caracteres")
    private String contactName;
    
    private String profilePicture;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @Size(min = 3, max = 100, message = "La ubicación debe tener entre 3 y 100 caracteres")
    private String location;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Formato de teléfono inválido")
    private String phone;
    
    @Pattern(regexp = "^https?://.*", message = "URL del sitio web inválida")
    private String website;
    
    @Size(max = 100, message = "La industria no puede exceder 100 caracteres")
    private String industry;
    
    @Pattern(regexp = "INDIVIDUAL|SMALL|MEDIUM|LARGE", message = "Tamaño de empresa inválido")
    private String companySize;
}