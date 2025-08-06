package br.com.constructease.constructease.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String msg) {
        super(msg);
    }
}