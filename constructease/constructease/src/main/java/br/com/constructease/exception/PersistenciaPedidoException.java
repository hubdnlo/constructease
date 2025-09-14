package br.com.constructease.exception;

public class PersistenciaPedidoException extends RuntimeException {
    public PersistenciaPedidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
