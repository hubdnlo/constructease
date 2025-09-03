package br.com.constructease.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste para PedidoNaoEncontradoException")
class PedidoNaoEncontradoExceptionTest {

    @Test
    @DisplayName("Deve retornar a mensagem corretamente")
    void mensagemCorreta() {
        PedidoNaoEncontradoException ex = new PedidoNaoEncontradoException("Pedido não encontrado");
        assertEquals("Pedido não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void herancaCorreta() {
        assertTrue(RuntimeException.class.isAssignableFrom(PedidoNaoEncontradoException.class));
    }
}