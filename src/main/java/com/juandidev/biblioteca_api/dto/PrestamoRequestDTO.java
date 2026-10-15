package com.juandidev.biblioteca_api.dto;

import jakarta.validation.constraints.NotNull;

public record PrestamoRequestDTO(
        @NotNull(message = "El id del cliente es obligatorio")
        Long clienteId,

        @NotNull(message = "El id del libro es obligatorio")
        Long libroId
) {
}
