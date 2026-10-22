package com.juandidev.biblioteca_api.controller;

import com.juandidev.biblioteca_api.dto.LibroRequestDTO;
import com.juandidev.biblioteca_api.dto.LibroResponseDTO;
import com.juandidev.biblioteca_api.service.ILibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
@RequiredArgsConstructor
@Tag(name = "2. Libros", description = "Operaciones CRUD para la gestión del catálogo de libros (Requiere Bearer Token)")
public class LibroController {

    private final ILibroService libroService;

    @GetMapping()
    @Operation(summary = "Listar todos los libros", description = "Devuelve el catálogo completo de libros de la biblioteca.")
    public List<LibroResponseDTO> listarLibros(){
        return libroService.listarLibros();
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo libro", description = "Registra un nuevo libro validando campos obligatorios e ISBN.")
    public LibroResponseDTO crearLibro(@Valid @RequestBody LibroRequestDTO dto){
        return libroService.crearLibro(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar libro por ID", description = "Devuelve los datos de un libro específico por su identificador.")
    public LibroResponseDTO buscarLibro(@PathVariable Long id){
        return libroService.buscarLibro(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro", description = "Actualiza los datos de un libro existente.")
    public LibroResponseDTO actualizarLibro(@Valid @RequestBody LibroRequestDTO dto, @PathVariable Long id){
        return libroService.actualizarLibro(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro", description = "Elimina un libro del catálogo por su ID.")
    public void eliminarLibro(@PathVariable Long id){
        libroService.eliminarLibro(id);
    }
}
