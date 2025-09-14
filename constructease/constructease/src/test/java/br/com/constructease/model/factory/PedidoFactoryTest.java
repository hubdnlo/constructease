package br.com.constructease.model.factory;

import br.com.constructease.model.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Testes unitários para PedidoFactory")
class PedidoFactoryTest {

    @Test
    @DisplayName("Deve criar pedido com ID incremental baseado na lista existente")
    void criarPedidoComIdIncremental() {
        Pedido p1 = new Pedido(1L, "Pedido 1");
        Pedido p2 = new Pedido(2L, "Pedido 2");

        Pedido novo = PedidoFactory.criarPedidoIncrementandoId("Novo Pedido", List.of(p1, p2));

        assertEquals(3L, novo.getId());
        assertEquals("Novo Pedido", novo.getDescricao());
    }

    @Test
    @DisplayName("Deve criar pedido com ID 1 quando lista está vazia")
    void criarPedidoComListaVazia() {
        Pedido novo = PedidoFactory.criarPedidoIncrementandoId("Primeiro Pedido", List.of());

        assertEquals(1L, novo.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao passar lista nula")
    void criarPedidoComListaNula() {
        assertThrows(IllegalArgumentException.class, () ->
                PedidoFactory.criarPedidoIncrementandoId("Erro", null));
    }
}