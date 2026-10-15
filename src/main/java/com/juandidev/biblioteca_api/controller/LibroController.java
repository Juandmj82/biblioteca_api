package com.juandidev.biblioteca_api.controller;

import com.juandidev.biblioteca_api.dto.LibroRequestDTO;
import com.juandidev.biblioteca_api.dto.LibroResponseDTO;
import com.juandidev.biblioteca_api.service.ILibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
@RequiredArgsConstructor
public class LibroController {

    private final ILibroService libroService;

    @GetMapping()
    public List<LibroResponseDTO> listarLibros(){
        return libroService.listarLibros();
    }

    @PostMapping("/crear")
    public LibroResponseDTO crearLibro(@Valid @RequestBody LibroRequestDTO dto){
        return libroService.crearLibro(dto);
    }

    @GetMapping("/{id}")
    public LibroResponseDTO buscarLibro(@PathVariable Long id){
        return libroService.buscarLibro(id);
    }

    @PutMapping("/{id}")
    public LibroResponseDTO actualizarLibro(@Valid @RequestBody LibroRequestDTO dto, @PathVariable Long id){
        return libroService.actualizarLibro(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarLibro(@PathVariable Long id){
        libroService.eliminarLibro(id);
    }
}
