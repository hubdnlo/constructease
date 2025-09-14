package br.com.constructease.exception;

public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(String msg) {
        super(msg);
    }

}