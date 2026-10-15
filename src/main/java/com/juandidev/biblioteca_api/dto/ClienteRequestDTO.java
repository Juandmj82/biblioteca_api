package com.juandidev.biblioteca_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(
        @NotBlank(message= "El nombre es obligatorio")
        @Size(min = 2, max =50, message = "El nombre debe tener mínimo 2 caracteres , máximo 50")
        String nombre,

        Long dni,

        @NotBlank(message= "El apellido es obligatorio")
        @Size(min = 2, max =50, message = "El apellido debe tener mínimo 2 caracteres , máximo 50")
        String apellido,

        @Email(message = "El email debe tener formato válido")
        String email,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(regexp = "\\+?\\d{10,12}", message = "El número debe tener mínimo 10 dígitos, máximo 12")
        String telefono,

        String direccion
) {
}
