package com.juandidev.biblioteca_api.dto;

import java.time.LocalDateTime;

public record PrestamoResponseDTO(
        Long id,
        Long clienteId,
        String nombreCliente,
        Long libroId,
        String tituloLibro,
        LocalDateTime fechaPrestamo,
        LocalDateTime fechaDevolucion

) {
}
