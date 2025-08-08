package br.com.constructease.constructease.exception;

public class PersistenciaEstoqueException extends RuntimeException {
    public PersistenciaEstoqueException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

