package br.com.constructease.exception;

public class EstoqueNaoEncontradoException extends RuntimeException {
    public EstoqueNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
