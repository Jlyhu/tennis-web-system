package com.fsteni58.tennis.exception;

public class CamposInvalidosException extends RuntimeException {
    public CamposInvalidosException(String mensaje) {
        super(mensaje);
    }
}