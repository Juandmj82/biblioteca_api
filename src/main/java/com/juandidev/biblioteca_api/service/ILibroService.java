package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.LibroRequestDTO;
import com.juandidev.biblioteca_api.dto.LibroResponseDTO;

import java.util.List;

public interface ILibroService {

    List<LibroResponseDTO> listarLibros();

    public LibroResponseDTO crearLibro(LibroRequestDTO dto);

    public LibroResponseDTO buscarLibro(Long id);

    public LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO dto);

    public void eliminarLibro(Long id);

}
