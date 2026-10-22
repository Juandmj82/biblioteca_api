package com.juandidev.biblioteca_api.controller;

import com.juandidev.biblioteca_api.dto.ClienteRequestDTO;
import com.juandidev.biblioteca_api.dto.ClienteResponseDTO;
import com.juandidev.biblioteca_api.service.IClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clientes")
@Tag(name = "3. Clientes", description = "Gestión de clientes y consulta de sus préstamos asociados (Requiere Bearer Token)")
public class ClienteController {

    private final IClienteService clienteService;

    @GetMapping()
    @Operation(summary = "Listar todos los clientes", description = "Devuelve la lista de clientes registrados junto con sus préstamos.")
    public List<ClienteResponseDTO> listarClientes(){
        return clienteService.listarClientes();
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo cliente", description = "Registra un cliente en la biblioteca validando datos requeridos.")
    public ClienteResponseDTO crearCliente(@Valid @RequestBody ClienteRequestDTO dto){
        return clienteService.crearCliente(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Devuelve el detalle de un cliente específico.")
    public ClienteResponseDTO buscarCliente(@PathVariable Long id){
        return clienteService.buscarCliente(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente.")
    public ClienteResponseDTO actualizarCliente(@Valid @RequestBody ClienteRequestDTO dto, @PathVariable Long id){
        return clienteService.actualizarCliente(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente de la base de datos por su ID.")
    public void eliminarCliente(@PathVariable Long id){
        clienteService.eliminarCliente(id);
    }
}
