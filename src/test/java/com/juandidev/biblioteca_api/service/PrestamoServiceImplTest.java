package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.PrestamoRequestDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;
import com.juandidev.biblioteca_api.exception.ClienteConPrestamoPendienteException;
import com.juandidev.biblioteca_api.exception.LibroYaPrestadoException;
import com.juandidev.biblioteca_api.exception.PrestamoNoEncontradoException;
import com.juandidev.biblioteca_api.exception.PrestamoYaDevueltoException;
import com.juandidev.biblioteca_api.model.Cliente;
import com.juandidev.biblioteca_api.model.Libro;
import com.juandidev.biblioteca_api.model.Prestamo;
import com.juandidev.biblioteca_api.repository.ClienteRepository;
import com.juandidev.biblioteca_api.repository.LibroRepository;
import com.juandidev.biblioteca_api.repository.PrestamoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//Le dice a JUnit: "Activa a Mockito en esta clase para que cree los clones automáticamente"
@ExtendWith(MockitoExtension.class)
public class PrestamoServiceImplTest {

    //1. se crean los 3 clones
    @Mock
    private PrestamoRepository prestamoRepo;

    @Mock
    private ClienteRepository clienteRepo;

    @Mock
    private  LibroRepository libroRepo;

    //2. El protagonista principal con los tres mocks(clones) inyectados
    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    @Test
    void crearPrestamo_deberiaLanzarExcepcion_cuandoLibroYaEstaPrestado() {
        // 1. Arrange (Preparar)
        PrestamoRequestDTO dto = new PrestamoRequestDTO(1L, 5L);
        when(prestamoRepo.existsByLibroIdAndFechaDevolucionIsNull(5L)).thenReturn(true);

        // 2 y 3. Act & Assert (Ejecutar y Verificar)
        assertThrows(LibroYaPrestadoException.class, () -> {
            prestamoService.crearPrestamo(dto);
        });
    }

    @Test
    void crearPrestamo_deberiaLanzarExcepcion_cuandoClienteTienePrestamoActivo() {
        // 1. Arrange (Preparar)
        PrestamoRequestDTO dto = new PrestamoRequestDTO(1L, 5L);
        // El libro SÍ está disponible (false)
        when(prestamoRepo.existsByLibroIdAndFechaDevolucionIsNull(5L)).thenReturn(false);
        // Pero el cliente YA tiene un préstamo activo (true)
        when(prestamoRepo.existsByClienteIdAndFechaDevolucionIsNull(1L)).thenReturn(true);

        // 2 y 3. Act & Assert (Ejecutar y Verificar)
        assertThrows(ClienteConPrestamoPendienteException.class, () -> {
            prestamoService.crearPrestamo(dto);
        });
    }

    @Test
    void crearPrestamo_deberiaCrearYRetornarDTO_cuandoDatosSonValidos() {
        // Arrange
        PrestamoRequestDTO dto = new PrestamoRequestDTO(1L, 5L);
        Cliente cliente = new Cliente(1L, "Juan", "Diego", 12345L, "juan@test.com", "3001234567", "Calle 1", null);
        Libro libro = new Libro(5L, "Cien Años de Soledad", "Gabo", "Novela", "978-0307474728");
        Prestamo guardado = new Prestamo(10L, cliente, libro, LocalDateTime.now(), null);

        when(prestamoRepo.existsByLibroIdAndFechaDevolucionIsNull(5L)).thenReturn(false);
        when(prestamoRepo.existsByClienteIdAndFechaDevolucionIsNull(1L)).thenReturn(false);
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(libroRepo.findById(5L)).thenReturn(Optional.of(libro));
        when(prestamoRepo.save(any(Prestamo.class))).thenReturn(guardado);

        // Act
        PrestamoResponseDTO resultado = prestamoService.crearPrestamo(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.id());
        assertEquals("Juan", resultado.nombreCliente());
        assertEquals("Cien Años de Soledad", resultado.tituloLibro());
        assertNull(resultado.fechaDevolucion());
    }
    @Test
    void devolverPrestamo_deberiaLanzarExcepcion_cuandoIdNoExiste() {
        // Arrange (Preparar)
        when(prestamoRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert (Ejecutar y Verificar)
        assertThrows(PrestamoNoEncontradoException.class, () -> {
            prestamoService.devolverPrestamo(99L);
        });
    }

    @Test
    void devolverPrestamo_deberiaLanzarExcepcion_cuandoPrestamoYaFueDevuelto(){
        Cliente cliente = new Cliente(1L, "Juan", "Diego", 12345L, "juan@test.com",
                "3001234567", "Calle 1", null);
        Libro libro = new Libro(5L, "Cien Años de Soledad",
                "Gabo", "Novela", "978-0307474728");

// El último valor NO es null, ¡ya tiene fecha!
        Prestamo yaDevuelto = new Prestamo(10L, cliente, libro, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));

        when(prestamoRepo.findById(10L)).thenReturn(Optional.of(yaDevuelto));

        assertThrows(PrestamoYaDevueltoException.class, ()->{
            prestamoService.devolverPrestamo(10L);

        });
    }

    @Test
    void devolverPrestamo_deberiaDevolverPrestamo_cuandoDatosSonValidos() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Juan", "Diego", 12345L, "juan@test.com", "3001234567", "Calle 1", null);
        Libro libro = new Libro(5L, "Cien Años de Soledad", "Gabo", "Novela", "978-0307474728");
        Prestamo activo = new Prestamo(10L, cliente, libro, LocalDateTime.now().minusDays(2), null);

        when(prestamoRepo.findById(10L)).thenReturn(Optional.of(activo));
        when(prestamoRepo.save(any(Prestamo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PrestamoResponseDTO resultado = prestamoService.devolverPrestamo(10L);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.id());
        assertNotNull(resultado.fechaDevolucion());
    }

}


