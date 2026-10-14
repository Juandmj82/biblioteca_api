package com.juandidev.biblioteca_api.repository;

import com.juandidev.biblioteca_api.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
