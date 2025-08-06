package br.com.constructease.constructease.exception;

public class ProdutoInexistenteException extends RuntimeException {
    public ProdutoInexistenteException(String msg) {
        super(msg);
    }
}