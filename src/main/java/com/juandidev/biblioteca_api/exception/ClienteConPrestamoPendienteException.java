package com.juandidev.biblioteca_api.exception;

public class ClienteConPrestamoPendienteException extends RuntimeException {
    public ClienteConPrestamoPendienteException(String mensaje){
        super(mensaje);
    }
}
