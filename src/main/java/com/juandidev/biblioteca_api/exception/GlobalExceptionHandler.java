package com.juandidev.biblioteca_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ErrorResponseLibreria clienteNoEncontrado(ClienteNoEncontradoException ex){
        return new ErrorResponseLibreria(ex.getMessage(), "Cliente no encontrado en la base de datos", LocalDateTime.now());
    }

    @ExceptionHandler(LibroNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ErrorResponseLibreria libroNoEncontrado(LibroNoEncontradoException ex){
        return new ErrorResponseLibreria(ex.getMessage(), "Libro no encontrado en la base de datos", LocalDateTime.now());
    }

    @ExceptionHandler(PrestamoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseLibreria prestamoNoEncontrado(PrestamoNoEncontradoException ex){
        return new ErrorResponseLibreria(ex.getMessage(), "Préstamo no encontrado en la base de datos", LocalDateTime.now());
    }

    @ExceptionHandler(PrestamoYaDevueltoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseLibreria prestamoYaDevuelto(PrestamoYaDevueltoException ex){
        return new ErrorResponseLibreria(ex.getMessage(), "Préstamo ya devuelto en la base de datos", LocalDateTime.now());
    }

    @ExceptionHandler(LibroYaPrestadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseLibreria LibroYaPrestado(LibroYaPrestadoException ex){
        return new ErrorResponseLibreria(ex.getMessage(),"Libro ya prestado", LocalDateTime.now());
    }

    @ExceptionHandler(ClienteConPrestamoPendienteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseLibreria clienteConPrestamo(ClienteConPrestamoPendienteException ex){
        return new ErrorResponseLibreria(ex.getMessage(), "El cliente ya tiene préstamo pendiente", LocalDateTime.now());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseLibreria datosInvalidos(MethodArgumentNotValidException ex){
        String problema = String.join(", ",
                ex.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList());

        return new ErrorResponseLibreria("Datos inválidos", problema, LocalDateTime.now());
    }


}
