package com.juandidev.biblioteca_api.exception;

public class LibroNoEncontradoException extends RuntimeException {
    public LibroNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
