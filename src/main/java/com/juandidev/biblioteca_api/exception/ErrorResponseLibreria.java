package com.juandidev.biblioteca_api.exception;

import java.time.LocalDateTime;

public record ErrorResponseLibreria(
        String mensaje,
        String detalle,
        LocalDateTime fecha
) {
}
