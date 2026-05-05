package com.desafio.exception;

public class CepNotFoundException extends RuntimeException {

    public CepNotFoundException(String cep) {
        super("CEP não encontrado: " + cep);
    }

    public CepNotFoundException(String cep, Throwable cause) {
        super("CEP não encontrado: " + cep, cause);
    }
}