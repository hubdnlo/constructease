package br.com.constructease.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para ItemPedido")
class ItemPedidoTest {

    @Test
    @DisplayName("Deve calcular corretamente o subtotal")
    void calcularSubtotal() {
        ItemPedido item = new ItemPedido(1L, 3, new BigDecimal("10.00"));
        BigDecimal subtotal = item.calcularSubtotal();
        assertEquals(new BigDecimal("30.00"), subtotal);
    }

    @Test
    @DisplayName("Deve retornar true em equals para objetos com mesmo ID")
    void equalsComMesmoId() {
        ItemPedido item1 = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        ItemPedido item2 = new ItemPedido(1L, 5, new BigDecimal("20.00"));
        item1.setId(100L);
        item2.setId(100L);

        assertEquals(item1, item2);
    }

    @Test
    @DisplayName("Deve retornar false em equals para objetos com IDs diferentes")
    void equalsComIdsDiferentes() {
        ItemPedido item1 = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        ItemPedido item2 = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        item1.setId(100L);
        item2.setId(200L);

        assertNotEquals(item1, item2);
    }

    @Test
    @DisplayName("Deve retornar false em equals para objeto de tipo diferente")
    void equalsComTipoDiferente() {
        ItemPedido item = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        item.setId(100L);

        assertNotEquals(item, "não é um ItemPedido");
    }

    @Test
    @DisplayName("Deve retornar true em equals para o mesmo objeto")
    void equalsComMesmoObjeto() {
        ItemPedido item = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        item.setId(100L);

        assertEquals(item, item);
    }

    @Test
    @DisplayName("Deve gerar hashCode consistente com equals")
    void hashCodeConsistenteComEquals() {
        ItemPedido item1 = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        ItemPedido item2 = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        item1.setId(300L);
        item2.setId(300L);

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar hashCode diferente para objetos com IDs diferentes")
    void hashCodeComIdsDiferentes() {
        ItemPedido item1 = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        ItemPedido item2 = new ItemPedido(1L, 2, new BigDecimal("10.00"));
        item1.setId(300L);
        item2.setId(301L);

        assertNotEquals(item1.hashCode(), item2.hashCode());
    }
}