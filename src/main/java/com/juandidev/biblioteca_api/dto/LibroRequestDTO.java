package com.juandidev.biblioteca_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LibroRequestDTO(
        @NotBlank(message = "El título es obligatorio")
        @Size(min = 2, max = 50, message = "El título debe tener mínimo 2 caracteres, máximo 50")
        String titulo,

        @NotBlank(message = "El nombre del autor es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre del autor debe tener mínimo 2 caracteres, máximo 50")
        String autor,

        @NotBlank(message = "El género es obligatorio")
        @Size(min = 2, max = 50, message = "El género debe tener mínimo 2 caracteres, máximo 50")
        String genero,

        String isbn
) {
}
