package br.com.constructease.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String msg) {
        super(msg);
    }
}