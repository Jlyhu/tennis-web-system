package com.fsteni58.tennis.exception;

public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
