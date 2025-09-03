package br.com.constructease.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste para JsonLeituraException")
class JsonLeituraExceptionTest {

    @Test
    @DisplayName("Deve armazenar mensagem e causa corretamente")
    void mensagemECausa() {
        Exception causa = new Exception("Erro de leitura");
        JsonLeituraException ex = new JsonLeituraException("Falha ao ler JSON", causa);

        assertEquals("Falha ao ler JSON", ex.getMessage());
        assertEquals(causa, ex.getCause());
    }
}

