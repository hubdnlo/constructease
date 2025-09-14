package br.com.constructease.exception;

public class JsonGravacaoException extends RuntimeException {
    public JsonGravacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
