package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.LibroRequestDTO;
import com.juandidev.biblioteca_api.dto.LibroResponseDTO;
import com.juandidev.biblioteca_api.exception.LibroNoEncontradoException;
import com.juandidev.biblioteca_api.model.Libro;
import com.juandidev.biblioteca_api.repository.LibroRepository;
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
public class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepo;

    @InjectMocks
    private LibroServiceImpl libroService;

    @Test
    void crearLibro_deberiaGuardarYRetornarDTO_cuandoDatosSonValidos(){
        //1.Arrange
        LibroRequestDTO dto = new LibroRequestDTO("La sombra del Viento", "Carlos Ruiz Zafón", "Suspenso", "1234qw");
        Libro guardado = new Libro(1L, "La sombra del Viento", "Carlos Ruiz Zafón", "Suspenso", "1234qw");

        when(libroRepo.save(any(Libro.class))).thenReturn(guardado);

        //2. actuar
        LibroResponseDTO resultado = libroService.crearLibro(dto);

        //3 verificar
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("La sombra del Viento", resultado.titulo());

    }

    @Test
    void buscarLibro_deberiaLanzarExcepcion_cuandoIdNoExiste(){
        //1. Arrange
        when(libroRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LibroNoEncontradoException.class,()->{
            libroService.buscarLibro(1L);
        } );
    }

    @Test
    void buscarLibro_deberiaRetornarDTO_cuandoIdExiste(){
        //Creamos el libro
        Libro libro = new Libro(1L, "La sombra del viento", "Carlos Ruiz Zafón","suspenso",  "1234qw");
        //1. Arrange
        when(libroRepo.findById(1L)).thenReturn(Optional.of(libro));

        //2.act

        LibroResponseDTO resultado = libroService.buscarLibro(1L);

        //3.Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("La sombra del viento", resultado.titulo());

    }

    @Test
    void actualizarLibro_deberiaRetornarDTO_siDatosSonValidos(){
        //1. Arrange. Creamos libro viejo y dtocon datos nuevos para actualizar
        Libro existente = new Libro(1L, "Cien años de soledad", "Gabriel García Marquez ", "viejo", "12345qw");
        LibroRequestDTO dtoNuevo = new LibroRequestDTO("Cien años de Soledad", "Gabriel García Marquez", "Nuevo", "12345qw");

        //entrenamos el repo mockito
        when(libroRepo.findById(1L)).thenReturn(Optional.of(existente));
        when(libroRepo.save(any(Libro.class))).thenAnswer(i-> i.getArgument(0));

        //2.Act
        LibroResponseDTO resultado = libroService.actualizarLibro(1L, dtoNuevo);

        //3. Assert
        assertNotNull(resultado);
        assertEquals("Nuevo", resultado.genero());
    }

    @Test
    void actualizarLibro_deberiaLanzarExcepcion_cuandoIdNoExiste(){
        LibroRequestDTO dtoNuevo = new LibroRequestDTO("Cien Años de soledad", "Gabriel García Márquez", "Nuevo", "123456qw");

        //1. Arrange
        when(libroRepo.findById(99L)).thenReturn(Optional.empty());

        //2 y 3 Act & Assert
        assertThrows(LibroNoEncontradoException.class, ()->{
            libroService.actualizarLibro(99L, dtoNuevo);
        });
    }



    @Test
    void eliminarLibro_deberiaLanzarExcepcion_cuandoIdNoExiste(){
        //1.Arrange
        when(libroRepo.findById(1L)).thenReturn(Optional.empty());

        //2.Ejecutar y verificar
        assertThrows(LibroNoEncontradoException.class, ()->{
            libroService.eliminarLibro(1L);
        });


        verify(libroRepo, never()).delete(any(Libro.class));


    }

}
