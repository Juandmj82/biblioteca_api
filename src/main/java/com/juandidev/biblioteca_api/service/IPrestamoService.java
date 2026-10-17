package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.PrestamoRequestDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;

import java.util.List;

public interface IPrestamoService {

    public PrestamoResponseDTO crearPrestamo(PrestamoRequestDTO dto);

    public PrestamoResponseDTO devolverPrestamo(Long id);

    public List<PrestamoResponseDTO> listarPrestamos();

    public PrestamoResponseDTO buscarPrestamo(Long id);

    public List<PrestamoResponseDTO> listarPrestamoPorCliente(Long clienteId);

}
