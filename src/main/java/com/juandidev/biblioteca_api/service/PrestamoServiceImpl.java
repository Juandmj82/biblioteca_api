package com.juandidev.biblioteca_api.service;


import com.juandidev.biblioteca_api.dto.PrestamoRequestDTO;
import com.juandidev.biblioteca_api.dto.PrestamoResponseDTO;
import com.juandidev.biblioteca_api.exception.*;
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

        //validaciones
        if (prestamoRepo.existsByLibroIdAndFechaDevolucionIsNull(dto.libroId())) {
            throw new LibroYaPrestadoException("El libro con id " + dto.libroId() + " ya está prestado");
        }

        if (prestamoRepo.existsByClienteIdAndFechaDevolucionIsNull(dto.clienteId())) {
            throw new ClienteConPrestamoPendienteException("El cliente con id " + dto.clienteId() + " ya tiene un préstamo activo");
        }
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

    @Override
    public PrestamoResponseDTO devolverPrestamo(Long id) {
        // 1) Buscar el préstamo por id. Si no existe, lanzar PrestamoNoEncontradoException
        Prestamo prestamo = prestamoRepo.findById(id).orElseThrow(()->new PrestamoNoEncontradoException("El préstamo con id " + id + " no encontrado" ));

        // 2) Si su fechaDevolucion ya NO es null, lanzar la excepción de "ya devuelto" (la creamos después)
        if(prestamo.getFechaDevolucion() != null ){
            throw new PrestamoYaDevueltoException("Préstamo con id " + id + " ya fue devuelto");
        }

        // 3) Ponerle fechaDevolucion = ahora (LocalDateTime.now())
        prestamo.setFechaDevolucion(LocalDateTime.now());

        // 4) Guardar y devolver convertirDTO(lo guardado)
        return convertirDTO(prestamoRepo.save(prestamo));
    }

    private PrestamoResponseDTO convertirDTO(Prestamo prestamo){
        return new PrestamoResponseDTO(prestamo.getId(),prestamo.getCliente().getId(),
                prestamo.getCliente().getNombre(), prestamo.getLibro().getId(), prestamo.getLibro().getTitulo(),
                prestamo.getFechaPrestamo(),prestamo.getFechaDevolucion());
    }
}
