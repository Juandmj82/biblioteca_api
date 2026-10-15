package com.juandidev.biblioteca_api.repository;

import com.juandidev.biblioteca_api.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
