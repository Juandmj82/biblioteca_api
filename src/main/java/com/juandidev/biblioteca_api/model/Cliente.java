package com.juandidev.biblioteca_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="clientes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String nombre;

    @Column(nullable=false, length=50)
    private String apellido;

    private Long dni;

    private String email;

    private String telefono;

    private String direccion;

    @OneToMany(mappedBy = "cliente")
    private List<Prestamo> prestamos;
}
