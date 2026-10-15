package com.juandidev.biblioteca_api.controller;

import com.juandidev.biblioteca_api.dto.ClienteRequestDTO;
import com.juandidev.biblioteca_api.dto.ClienteResponseDTO;
import com.juandidev.biblioteca_api.service.IClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clientes")
public class ClienteController {

    //inyectamos el servicio
    private final IClienteService clienteService;

    @GetMapping()
    public List<ClienteResponseDTO> listarClientes(){
        return clienteService.listarClientes();
    }

    @PostMapping("/crear")
    public ClienteResponseDTO crearCliente(@Valid @RequestBody ClienteRequestDTO dto){
        return clienteService.crearCliente(dto);
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscarCliente(@PathVariable Long id){
        return clienteService.buscarCliente(id);
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizarCliente(@Valid @RequestBody ClienteRequestDTO dto, @PathVariable Long id){
        return clienteService.actualizarCliente(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id){
        clienteService.eliminarCliente(id);
    }
}
