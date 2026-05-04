package com.desafio.exception;

public class CepNotFoundException extends RuntimeException {
    public CepNotFoundException(String message) {
        super(message);
    }
}