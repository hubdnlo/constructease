package br.com.constructease.constructease.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Deve retornar 404 e mensagem ao tratar ProdutoInexistenteException")
    void handleProdutoInexistente() {
        String mensagem = "Produto não encontrado";
        ProdutoInexistenteException ex = new ProdutoInexistenteException(mensagem);

        ResponseEntity<String> response = handler.handleProdutoInexistente(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(mensagem, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 400 e mensagem ao tratar EstoqueInsuficienteException")
    void handleEstoqueInsuficiente() {
        String mensagem = "Estoque insuficiente";
        EstoqueInsuficienteException ex = new EstoqueInsuficienteException(mensagem);

        ResponseEntity<String> response = handler.handleEstoqueInsuficiente(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensagem, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 404 e mensagem ao tratar PedidoNaoEncontradoException")
    void handlePedidoNaoEncontrado() {
        String mensagem = "Pedido não encontrado";
        PedidoNaoEncontradoException ex = new PedidoNaoEncontradoException(mensagem);

        ResponseEntity<String> response = handler.handlePedidoNaoEncontrado(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(mensagem, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 500 e mensagem genérica ao tratar exceção genérica")
    void handleGenericException() {
        Exception ex = new Exception("Falha inesperada");

        ResponseEntity<String> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro inesperado: Falha inesperada", response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 500 e mensagem genérica mesmo com exceção sem mensagem")
    void handleGenericExceptionSemMensagem() {
        Exception ex = new Exception();

        ResponseEntity<String> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro inesperado: null", response.getBody());
    }
}