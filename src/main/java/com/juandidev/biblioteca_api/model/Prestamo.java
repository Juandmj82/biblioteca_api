package com.juandidev.biblioteca_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prestamo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne @JoinColumn(name = "libro_id")
    private Libro libro;

    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;


}
