package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.ClienteRequestDTO;
import com.juandidev.biblioteca_api.dto.ClienteResponseDTO;
import com.juandidev.biblioteca_api.model.Cliente;

import java.util.List;

public interface IClienteService {

    List<ClienteResponseDTO> listarClientes();

    public ClienteResponseDTO crearCliente(ClienteRequestDTO dto);

    public ClienteResponseDTO buscarCliente(Long id);

    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO dto);

    public void eliminarCliente(Long id);
}
