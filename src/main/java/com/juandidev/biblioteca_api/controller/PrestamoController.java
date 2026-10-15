package com.juandidev.biblioteca_api.controller;

import com.juandidev.biblioteca_api.dto.PrestamoRequestDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;
import com.juandidev.biblioteca_api.service.IPrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
