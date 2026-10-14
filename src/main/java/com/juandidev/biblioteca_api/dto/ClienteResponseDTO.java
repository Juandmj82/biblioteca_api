package com.juandidev.biblioteca_api.dto;

public record ClienteResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String email

) {
}
