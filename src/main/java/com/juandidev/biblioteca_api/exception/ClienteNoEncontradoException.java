package com.juandidev.biblioteca_api.exception;

public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
