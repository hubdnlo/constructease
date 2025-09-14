package br.com.constructease.exception;

public class ProdutoInexistenteException extends RuntimeException {
    public ProdutoInexistenteException(String msg) {
        super(msg);
    }
}