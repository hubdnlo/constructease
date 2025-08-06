package br.com.constructease.constructease.integration;

import br.com.constructease.constructease.dto.ItemPedidoDTO;
import br.com.constructease.constructease.dto.PedidoDTO;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.service.PedidoService;
import br.com.constructease.constructease.service.EstoqueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PedidoIntegrationTest {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private EstoqueService estoqueService;

    @Test
    void fluxoCompleto_deveCriarCancelarEPersistirPedido() {
        PedidoDTO dto = new PedidoDTO("Pedido Integração", List.of(new ItemPedidoDTO(1, 1)));
        Pedido pedido = pedidoService.criarPedido(dto);
        assertNotNull(pedido.getId());

        pedidoService.cancelarPedido(pedido.getId());
        assertEquals("CANCELADO", pedido.getStatus().name());
    }
}

