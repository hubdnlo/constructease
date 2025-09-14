package br.com.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Teste para JsonGravacaoException")
class JsonGravacaoExceptionTest {

    @Test
    @DisplayName("Deve armazenar mensagem e causa corretamente")
    void mensagemECausa() {
        Exception causa = new Exception("Erro de escrita");
        JsonGravacaoException ex = new JsonGravacaoException("Falha ao gravar JSON", causa);

        assertEquals("Falha ao gravar JSON", ex.getMessage());
        assertEquals(causa, ex.getCause());
    }
}