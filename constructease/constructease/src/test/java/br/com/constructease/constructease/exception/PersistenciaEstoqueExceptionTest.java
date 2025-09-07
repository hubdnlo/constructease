package br.com.constructease.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Teste para PersistenciaEstoqueException")
class PersistenciaEstoqueExceptionTest {

    @Test
    @DisplayName("Deve armazenar mensagem e causa corretamente")
    void mensagemECausa() {
        Exception causa = new Exception("Falha de I/O");
        PersistenciaEstoqueException ex = new PersistenciaEstoqueException("Erro ao gravar", causa);

        assertEquals("Erro ao gravar", ex.getMessage());
        assertEquals(causa, ex.getCause());
    }
}