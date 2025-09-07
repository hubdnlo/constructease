package br.com.constructease.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Teste para PersistenciaPedidoException")
class PersistenciaPedidoExceptionTest {

    @Test
    @DisplayName("Deve armazenar mensagem e causa corretamente")
    void mensagemECausa() {
        Exception causa = new Exception("Falha ao salvar");
        PersistenciaPedidoException ex = new PersistenciaPedidoException("Erro ao persistir pedido", causa);

        assertEquals("Erro ao persistir pedido", ex.getMessage());
        assertEquals(causa, ex.getCause());
    }
}