package com.juandidev.biblioteca_api.exception;

public class LibroYaPrestadoException extends RuntimeException {
    public LibroYaPrestadoException(String mensaje){
        super(mensaje);
    }
}
