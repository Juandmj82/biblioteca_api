package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.ClienteRequestDTO;
import com.juandidev.biblioteca_api.dto.ClienteResponseDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;
import com.juandidev.biblioteca_api.exception.ClienteNoEncontradoException;
import com.juandidev.biblioteca_api.model.Cliente;
import com.juandidev.biblioteca_api.model.Prestamo;
import com.juandidev.biblioteca_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements IClienteService {


    private final ClienteRepository clienteRepo;

    @Override
    public List<ClienteResponseDTO> listarClientes() {
        return clienteRepo.findAll().stream()
                .map(this::convertirDTO).toList();
    }

    @Override
    public ClienteResponseDTO crearCliente(ClienteRequestDTO dto) {
        //se crea el nuevo cliente
        Cliente nuevoCliente = new Cliente(
                null, dto.nombre(),
                dto.apellido(),
                dto.dni(),
                dto.email(),
                dto.telefono(),
                dto.direccion(),
                null);
        //se guarda el nuevo cliente en una variable
        Cliente guardado = clienteRepo.save(nuevoCliente);
        return convertirDTO(guardado); //se convierte el cliente guardado a dto
    }

    @Override
    public ClienteResponseDTO buscarCliente(Long id) {
        return convertirDTO(obtenerCliente(id));
    }

    @Override
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO dto) {
        //Buscamos el cliente usando el método propio
        Cliente encontrado = obtenerCliente(id);

        //modificamos los datos obteniéndolos a través del dto
        encontrado.setNombre(dto.nombre());
        encontrado.setApellido(dto.apellido());
        encontrado.setDni(dto.dni());
        encontrado.setEmail(dto.email());
        encontrado.setTelefono(dto.telefono());
        encontrado.setDireccion(dto.direccion());

        //guardamos los datos modificados y pasamos el cliente al convertidor a dto
        return convertirDTO(clienteRepo.save(encontrado));
    }

    @Override
    public void eliminarCliente(Long id) {
        Cliente encontrado = obtenerCliente(id);
        clienteRepo.delete(encontrado);

    }

    //este método lo que permite es convertir la entidad a dto
    private ClienteResponseDTO convertirDTO(Cliente cliente){
        List<PrestamoResponseDTO> listaPrestamos =(cliente.getPrestamos()== null)
                ? List.of()
                :cliente.getPrestamos().stream()
                .map(this::convertirPrestamoDTO)
                .toList();
        return new ClienteResponseDTO(cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                listaPrestamos);
    }
// método propio para obtener el cliente y retornar excepción personalizada
    private  Cliente obtenerCliente(Long id){
        return clienteRepo.findById(id).orElseThrow(()-> new ClienteNoEncontradoException("Cliente con id " + id + " no encontrado"));
    }

    //"A través de cliente vamos a enviar la lista de préstamos que tiene el cliente,
    // pero no podemos enviar toda la entidad sino que esa lista se tiene que mapear
    // para determinar qué sí se va a mostrar de esos préstamos."
    private PrestamoResponseDTO  convertirPrestamoDTO(Prestamo prestamo){
        return new PrestamoResponseDTO(
                prestamo.getId(),
                prestamo.getCliente().getId(),
                prestamo.getCliente().getNombre(),
                prestamo.getLibro().getId(),
                prestamo.getLibro().getTitulo(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion()
        );
    }
}
