package br.com.constructease.constructease.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para Pedido")
class PedidoTest {

    @Test
    @DisplayName("Deve calcular corretamente o valor total com múltiplos itens")
    void calcularValorTotalComItens() {
        ItemPedido item1 = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        ItemPedido item2 = new ItemPedido(2L, 3, new BigDecimal("5.00"));
        Pedido pedido = new Pedido("Pedido de teste");
        pedido.setItens(List.of(item1, item2));

        BigDecimal total = pedido.calcularValorTotal();
        assertEquals(new BigDecimal("35.00"), total);
    }

    @Test
    @DisplayName("Deve retornar zero ao calcular valor total de pedido sem itens")
    void calcularValorTotalSemItens() {
        Pedido pedido = new Pedido("Pedido vazio");
        pedido.setItens(List.of());

        BigDecimal total = pedido.calcularValorTotal();
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    @DisplayName("Deve calcular valor total com item de quantidade zero")
    void calcularValorTotalComQuantidadeZero() {
        ItemPedido item = new ItemPedido(1L, 0, new BigDecimal("10.00"));
        Pedido pedido = new Pedido("Pedido com item zerado");
        pedido.setItens(List.of(item));

        BigDecimal total = pedido.calcularValorTotal();

        assertTrue(total.compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    @DisplayName("Deve calcular valor total com item de preço zero")
    void calcularValorTotalComPrecoZero() {
        ItemPedido item = new ItemPedido(1L, 5, BigDecimal.ZERO);
        Pedido pedido = new Pedido("Pedido com preço zero");
        pedido.setItens(List.of(item));

        BigDecimal total = pedido.calcularValorTotal();
        assertEquals(BigDecimal.ZERO, total);
    }
}