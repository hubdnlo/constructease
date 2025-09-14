package br.com.constructease.exception;

public class PersistenciaEstoqueException extends RuntimeException {
    public PersistenciaEstoqueException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

