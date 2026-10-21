package com.juandidev.biblioteca_api.dto.auth;

import com.juandidev.biblioteca_api.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistroRequestDTO(

        @NotBlank(message= "El email o puedes estar vacío")
        @Email(message = "El formato del email no es válido")
        String email,

        @NotBlank(message= "La contraseña no puede estar vacía")
        @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
        String password,

        @NotNull(message = "El rol es obligatorio")
        Rol rol
) {
}
