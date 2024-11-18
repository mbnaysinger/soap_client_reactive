package com.pedidovenda.pedido.api.v1.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the full stack trace for debugging
        ex.printStackTrace();
        return new ResponseEntity<>("Ocorreu um erro ao processar sua requisição. " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handler específico para NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerExceptions(NullPointerException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Um erro inesperado ocorreu. Por favor, verifique os dados enviados.", HttpStatus.BAD_REQUEST);
    }
}