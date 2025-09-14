package br.com.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Teste para EstoqueInsuficienteException")
class EstoqueInsuficienteExceptionTest {

    @Test
    @DisplayName("Deve armazenar a mensagem corretamente")
    void mensagemCorreta() {
        EstoqueInsuficienteException ex = new EstoqueInsuficienteException("Estoque insuficiente");
        assertEquals("Estoque insuficiente", ex.getMessage());
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void herancaCorreta() {
        assertTrue(RuntimeException.class.isAssignableFrom(EstoqueInsuficienteException.class));
    }
}