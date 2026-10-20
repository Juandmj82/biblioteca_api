package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.ClienteRequestDTO;
import com.juandidev.biblioteca_api.dto.ClienteResponseDTO;
import com.juandidev.biblioteca_api.exception.ClienteNoEncontradoException;
import com.juandidev.biblioteca_api.model.Cliente;
import com.juandidev.biblioteca_api.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepo;

    @InjectMocks()
    private ClienteServiceImpl clienteService;

    @Test
    void crearCliente_deberiaGuardarYRetornarDTO_cuandoDatosSonValidos(){
        //1.Arrange
        ClienteRequestDTO dto = new ClienteRequestDTO("Juan", "Merchán", 23456789L, "juan@test.com", "3243456789", "calle 1");
        Cliente guardado = new Cliente (1L,"Juan", "Merchán",23456789L, "juan@test.com","3243456789", "Calle 1", null);

        when(clienteRepo.save(any(Cliente.class))).thenReturn(guardado);

        //2. Act(Ejecutar)
        ClienteResponseDTO resultado = clienteService.crearCliente(dto);

        //3.Assert(verificar)
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Juan", resultado.nombre());
    }

    @Test
    void buscarCliente_deberiaLanzarExcepcion_cuandoIdNoExiste(){
        //1.arrange
        when(clienteRepo.findById(99L)).thenReturn(Optional.empty());
        //2 y 3 Act & Assert
        assertThrows(ClienteNoEncontradoException.class, ()->{
            clienteService.buscarCliente(99L);

        });

    }

    @Test
    void buscarCliente_deberiaRetornarDTO_cuandoIdExiste(){
        //creamos el cliente
        Cliente cliente = new Cliente(1L,"Juan", "Merchán", 12345678L, "juan@test.com","3001234567", "calle 1", null);

        //1. Preparamos
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        //2.Ejecutamos
        ClienteResponseDTO resultado = clienteService.buscarCliente(1L);
        //3.Verificamos
        assertNotNull(resultado);
        assertEquals(1L,resultado.id());
        assertEquals("Juan", resultado.nombre());

    }

    @Test
    void actualizarCliente_deberiaRetornarDTO_siDatosSonValidos(){
        //1. Arrange: un cliente viejo con id 1L
        //El dto con los datos nuevos a actualiar
        Cliente existente = new Cliente(1L, "Juan", "Viejo", 12345678L, "viejo@test.com","3000000000","Calle 1", null);
        ClienteRequestDTO dtoNuevo = new ClienteRequestDTO("Juan", "Actualizado", 12345678L, "nuevo@test.com","3111111111", "Calle 2");

        //Entrenamos el repo de mockito
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(existente));
        when(clienteRepo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        //2.act
        ClienteResponseDTO resultado = clienteService.actualizarCliente(1L,dtoNuevo);

        //3. Assert
        assertNotNull(resultado);
        assertEquals("Actualizado", resultado.apellido());

    }

    @Test
    void actualizarCliente_deberiaLanzarExcepcion_cuandoIdNoExiste(){
        ClienteRequestDTO  dtoNuevo = new ClienteRequestDTO("Juan", "Merchán", 1234567L, "Juan@test.com", "324567890", "Calle 3");
        //1. Arrange
        when(clienteRepo.findById(1L)).thenReturn(Optional.empty());
        //2. Act y assert
        assertThrows(ClienteNoEncontradoException.class, ()->{
            clienteService.actualizarCliente(1L, dtoNuevo);
        });
    }

    @Test
    void eliminarCliente_deberiaLanzarExcepcion_cuandoIdNoExiste(){
        //1.Preparar
        when(clienteRepo.findById(99L)).thenReturn(Optional.empty());

        //2 ejecutar y verificar
        assertThrows(ClienteNoEncontradoException.class, ()->{
            clienteService.eliminarCliente(99L);
        });

        //Verificar que NUNCA intentó borrar nada
        verify(clienteRepo, never()).delete(any(Cliente.class));


    }}
