package com.juandidev.biblioteca_api.dto;

public record LibroResponseDTO(
        Long id,
        String titulo,
        String autor,
        String genero,
        String isbn
) {
}
