package com.juandidev.biblioteca_api.controller;

import com.juandidev.biblioteca_api.dto.PrestamoRequestDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;
import com.juandidev.biblioteca_api.service.IPrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final IPrestamoService prestamoService;

            @PostMapping
            public PrestamoResponseDTO crearPrestamo(@Valid @RequestBody PrestamoRequestDTO dto){
            return prestamoService.crearPrestamo(dto);
            }

            @PutMapping("/{id}/devolver")
            public PrestamoResponseDTO devolverPrestamo(@PathVariable Long id){
                return prestamoService.devolverPrestamo(id);
            }

            @GetMapping()
            public List<PrestamoResponseDTO> listarPrestamos(){
                return prestamoService.listarPrestamos();
            }

            @GetMapping("/{id}")
            public PrestamoResponseDTO buscarPrestamo(@PathVariable Long id){
                return prestamoService.buscarPrestamo(id);
            }

            @GetMapping("/cliente/{clienteId}")
            public List<PrestamoResponseDTO> listarPrestamosPorCliente(@PathVariable Long clienteId){
                return prestamoService.listarPrestamoPorCliente(clienteId);
            }
}
