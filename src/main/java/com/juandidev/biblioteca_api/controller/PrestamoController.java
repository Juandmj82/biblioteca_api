package com.juandidev.biblioteca_api.controller;

import com.juandidev.biblioteca_api.dto.PrestamoRequestDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;
import com.juandidev.biblioteca_api.service.IPrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
@Tag(name = "4. Préstamos", description = "Reglas de negocio: préstamos activos y devoluciones (Requiere Bearer Token)")
public class PrestamoController {

    private final IPrestamoService prestamoService;

    @PostMapping
    @Operation(summary = "Crear préstamo", description = "Registra un préstamo validando que el libro no esté prestado y el cliente no tenga deudas.")
    public PrestamoResponseDTO crearPrestamo(@Valid @RequestBody PrestamoRequestDTO dto){
        return prestamoService.crearPrestamo(dto);
    }

    @PutMapping("/{id}/devolver")
    @Operation(summary = "Devolver préstamo", description = "Marca un préstamo como devuelto. Si ya fue devuelto, retorna 409 Conflict.")
    public PrestamoResponseDTO devolverPrestamo(@PathVariable Long id){
        return prestamoService.devolverPrestamo(id);
    }

    @GetMapping()
    @Operation(summary = "Listar todos los préstamos", description = "Obtiene la lista histórica de todos los préstamos registrados.")
    public List<PrestamoResponseDTO> listarPrestamos(){
        return prestamoService.listarPrestamos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar préstamo por ID", description = "Consulta los detalles de un préstamo específico.")
    public PrestamoResponseDTO buscarPrestamo(@PathVariable Long id){
        return prestamoService.buscarPrestamo(id);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar préstamos por cliente", description = "Devuelve todos los préstamos asociados a un cliente en particular.")
    public List<PrestamoResponseDTO> listarPrestamosPorCliente(@PathVariable Long clienteId){
        return prestamoService.listarPrestamoPorCliente(clienteId);
    }
}
