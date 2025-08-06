package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.model.StatusPedido;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedidoRepositoryTest {

    private final PedidoRepository pedidoRepository = new PedidoRepository();

    @Test
    void deveSalvarEPesquisarPedidoComReflexao() throws Exception {
        // Instancia Pedido via reflexão usando construtor protegido
        Constructor<Pedido> constructor = Pedido.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Pedido pedido = constructor.newInstance();

        // Preenche os dados manualmente
        pedido.setId(99L);
        pedido.setDescricao("Pedido de teste");
        pedido.setStatus(StatusPedido.ATIVO);
        pedido.setAtivo(true);

        // Salva o pedido individualmente
        pedidoRepository.save(pedido);

        // Verifica se foi salvo corretamente
        Optional<Pedido> encontrado = pedidoRepository.findOptionalById(99L);
        assertTrue(encontrado.isPresent());
        assertEquals("Pedido de teste", encontrado.get().getDescricao());
        assertEquals(StatusPedido.ATIVO, encontrado.get().getStatus());
    }
}
