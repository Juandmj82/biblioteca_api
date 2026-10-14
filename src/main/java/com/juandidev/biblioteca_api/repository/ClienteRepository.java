package com.juandidev.biblioteca_api.repository;

import com.juandidev.biblioteca_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
