package br.com.constructease.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Teste para ProdutoInexistenteException")
class ProdutoInexistenteExceptionTest {

    @Test
    @DisplayName("Deve armazenar e retornar a mensagem corretamente")
    void mensagemCorreta() {
        ProdutoInexistenteException ex = new ProdutoInexistenteException("Produto não encontrado");
        assertEquals("Produto não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void herancaCorreta() {
        assertTrue(RuntimeException.class.isAssignableFrom(ProdutoInexistenteException.class));
    }
}