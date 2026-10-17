package com.juandidev.biblioteca_api.repository;

import com.juandidev.biblioteca_api.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    boolean existsByClienteIdAndFechaDevolucionIsNull(Long clienteId);
    boolean existsByLibroIdAndFechaDevolucionIsNull(Long libroId);
    List<Prestamo> findByClienteId(Long clienteId);
}
