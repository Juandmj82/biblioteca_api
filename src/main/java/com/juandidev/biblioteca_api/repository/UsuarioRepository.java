package com.juandidev.biblioteca_api.repository;

import com.juandidev.biblioteca_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //método para porder buscar a usuarios por su identificador único (username)
    Optional<Usuario> findByEmail(String email);
}
