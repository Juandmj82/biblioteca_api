package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.PrestamoRequestDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;

public interface IPrestamoService {

    public PrestamoResponseDTO crearPrestamo(PrestamoRequestDTO dto);

    public PrestamoResponseDTO devolverPrestamo(Long id);

}
