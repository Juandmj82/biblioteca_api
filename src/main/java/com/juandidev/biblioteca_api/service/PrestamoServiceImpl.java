package com.juandidev.biblioteca_api.service;


import com.juandidev.biblioteca_api.dto.PrestamoRequestDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;
import com.juandidev.biblioteca_api.exception.ClienteNoEncontradoException;
import com.juandidev.biblioteca_api.exception.LibroNoEncontradoException;
import com.juandidev.biblioteca_api.model.Cliente;
import com.juandidev.biblioteca_api.model.Libro;
import com.juandidev.biblioteca_api.model.Prestamo;
import com.juandidev.biblioteca_api.repository.ClienteRepository;
import com.juandidev.biblioteca_api.repository.LibroRepository;
import com.juandidev.biblioteca_api.repository.PrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements IPrestamoService {

    private final PrestamoRepository prestamoRepo;
    private final ClienteRepository clienteRepo;
    private final LibroRepository libroRepo;


    @Override
    public PrestamoResponseDTO crearPrestamo(PrestamoRequestDTO dto) {
        // 1) Buscar el cliente con dto.clienteId(). Si no existe, lanzar ClienteNoEncontradoException

        Cliente cliente = clienteRepo.findById(dto.clienteId())
                .orElseThrow(()-> new ClienteNoEncontradoException("Cliente con id " + dto.clienteId() + " no encontrado"));

        // 2) Buscar el libro con dto.libroId(). Si no existe, lanzar LibroNoEncontradoException
        Libro libro = libroRepo.findById(dto.libroId()).
                orElseThrow(()-> new LibroNoEncontradoException("Libro con id " + dto.libroId()+ " no encontrado"));

        // 3) Armar el Prestamo con sus 5 valores: id (null), cliente, libro, fecha de préstamo (ahora), fecha de devolución (null)

        Prestamo nuevo = new Prestamo(null,cliente, libro, LocalDateTime.now(), null );

        // 4) Guardar con prestamoRepo y quedarte con lo que devuelve el save

        Prestamo guardado = prestamoRepo.save(nuevo);

        // 5) Devolver el resultado ya convertido, con convertirDTO

        return convertirDTO(guardado);
    }

    private PrestamoResponseDTO convertirDTO(Prestamo prestamo){
        return new PrestamoResponseDTO(prestamo.getId(),prestamo.getCliente().getId(),
                prestamo.getCliente().getNombre(), prestamo.getLibro().getId(), prestamo.getLibro().getTitulo(),
                prestamo.getFechaPrestamo(),prestamo.getFechaDevolucion());
    }
}
