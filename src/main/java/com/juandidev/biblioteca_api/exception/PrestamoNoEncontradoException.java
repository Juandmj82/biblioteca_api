package com.juandidev.biblioteca_api.exception;

public class PrestamoNoEncontradoException extends RuntimeException {
    public PrestamoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
