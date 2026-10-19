package com.juandidev.biblioteca_api.dto;

import java.util.List;

public record ClienteResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String email,
        List<PrestamoResponseDTO> prestamo

) {
}
