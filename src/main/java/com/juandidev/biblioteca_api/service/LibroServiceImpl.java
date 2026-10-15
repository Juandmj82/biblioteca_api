package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.LibroRequestDTO;
import com.juandidev.biblioteca_api.dto.LibroResponseDTO;
import com.juandidev.biblioteca_api.exception.LibroNoEncontradoException;
import com.juandidev.biblioteca_api.model.Libro;
import com.juandidev.biblioteca_api.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements ILibroService {

    //inyectamos el repositorio

    private final LibroRepository libroRepo;


    @Override
    public List<LibroResponseDTO> listarLibros() {
        return libroRepo.findAll().stream()
                .map(this::convertirDTO).toList();
    }

    @Override
    public LibroResponseDTO crearLibro(LibroRequestDTO dto) {

        Libro nuevoLibro = new Libro(null, dto.titulo(), dto.autor(), dto.genero(), dto.isbn());

        Libro guardado = libroRepo.save(nuevoLibro);
        return convertirDTO(guardado);
    }

    @Override
    public LibroResponseDTO buscarLibro(Long id) {
        return convertirDTO(obtenerLibro(id));
    }

    @Override
    public LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO dto) {
        Libro encontrado = obtenerLibro(id);

        //modificamos los atributos
        encontrado.setTitulo(dto.titulo());
        encontrado.setAutor(dto.autor());
        encontrado.setGenero(dto.genero());
        encontrado.setIsbn(dto.isbn());

        return convertirDTO(libroRepo.save(encontrado));
    }

    @Override
    public void eliminarLibro(Long id) {
        Libro encontrado = obtenerLibro(id);
        libroRepo.delete(encontrado);

    }

    public LibroResponseDTO convertirDTO(Libro libro){
        return new LibroResponseDTO(libro.getId(),libro.getTitulo(), libro.getAutor(), libro.getGenero(),libro.getIsbn());
    }

    public Libro obtenerLibro(Long id){
        return libroRepo.findById(id).orElseThrow(() -> new LibroNoEncontradoException("Libro con id " + id + " no encontrado"));
    }
}
