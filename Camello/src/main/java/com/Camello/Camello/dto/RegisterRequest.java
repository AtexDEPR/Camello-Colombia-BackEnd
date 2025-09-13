package com.Camello.Camello.dto;

import com.Camello.Camello.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
    
    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmPassword;
    
    @NotNull(message = "El rol es obligatorio")
    private User.UserRole role;
    
    // Campos específicos para freelancer
    private String firstName;
    private String lastName;
    private String title;
    private String description;
    private String location;
    private String phone;
    
    // Campos específicos para contratante
    private String companyName;
    private String contactName;
    private String industry;
    private String website;
} 