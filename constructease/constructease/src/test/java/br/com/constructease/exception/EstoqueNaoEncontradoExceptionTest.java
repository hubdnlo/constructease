package br.com.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para EstoqueNaoEncontradoException")
class EstoqueNaoEncontradoExceptionTest {

    @Test
    @DisplayName("Deve instanciar a exceção com a mensagem fornecida")
    void deveInstanciarComMensagem() {
        String mensagem = "Estoque não encontrado para o produto ID 99";
        EstoqueNaoEncontradoException ex = new EstoqueNaoEncontradoException(mensagem);

        assertEquals(mensagem, ex.getMessage());
    }

    @Test
    @DisplayName("Deve ser uma subclasse de RuntimeException")
    void deveSerSubclasseDeRuntimeException() {
        EstoqueNaoEncontradoException ex = new EstoqueNaoEncontradoException("Erro");

        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve permitir instanciamento via reflexão com mensagem")
    void deveInstanciarViaReflexao() throws Exception {
        Constructor<EstoqueNaoEncontradoException> constructor =
                EstoqueNaoEncontradoException.class.getDeclaredConstructor(String.class);

        String mensagem = "Reflexão: estoque não encontrado";
        EstoqueNaoEncontradoException ex = constructor.newInstance(mensagem);

        assertNotNull(ex);
        assertEquals(mensagem, ex.getMessage());
    }

    @Test
    @DisplayName("Mensagem da exceção não deve ser nula quando fornecida")
    void mensagemNaoDeveSerNula() {
        EstoqueNaoEncontradoException ex = new EstoqueNaoEncontradoException("Mensagem de erro");

        assertNotNull(ex.getMessage());
    }

    @Test
    @DisplayName("Mensagem da exceção pode ser nula se passado null")
    void mensagemPodeSerNula() {
        EstoqueNaoEncontradoException ex = new EstoqueNaoEncontradoException(null);

        assertNull(ex.getMessage());
    }
}