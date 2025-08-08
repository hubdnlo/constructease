package br.com.constructease.constructease.exception;

public class JsonGravacaoException extends RuntimeException {
    public JsonGravacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
