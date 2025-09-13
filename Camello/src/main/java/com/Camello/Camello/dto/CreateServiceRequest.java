package com.Camello.Camello.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceRequest {
    
    @NotNull(message = "La categoría es obligatoria")
    private UUID categoryId;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 10, max = 100, message = "El título debe tener entre 10 y 100 caracteres")
    private String title;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 50, max = 2000, message = "La descripción debe tener entre 50 y 2000 caracteres")
    private String description;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "5000.0", message = "El precio mínimo es $5,000 COP")
    @DecimalMax(value = "10000000.0", message = "El precio máximo es $10,000,000 COP")
    private BigDecimal price;
    
    @NotNull(message = "El tiempo de entrega es obligatorio")
    @Min(value = 1, message = "El tiempo mínimo de entrega es 1 día")
    @Max(value = 365, message = "El tiempo máximo de entrega es 365 días")
    private Integer deliveryTime;
    
    @Min(value = 0, message = "Las revisiones no pueden ser negativas")
    @Max(value = 10, message = "Máximo 10 revisiones incluidas")
    private Integer revisionsIncluded = 1;
    
    @Size(max = 5, message = "Máximo 5 imágenes por servicio")
    private List<String> images;
    
    @Size(max = 10, message = "Máximo 10 tags por servicio")
    private List<String> tags;
}