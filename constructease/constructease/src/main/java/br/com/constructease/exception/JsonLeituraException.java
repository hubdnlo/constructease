package br.com.constructease.exception;

public class JsonLeituraException extends RuntimeException {
    public JsonLeituraException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
