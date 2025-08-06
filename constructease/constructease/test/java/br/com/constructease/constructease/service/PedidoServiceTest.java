package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ItemPedidoDTO;
import br.com.constructease.constructease.dto.PedidoDTO;
import br.com.constructease.constructease.exception.PedidoNaoEncontradoException;
import br.com.constructease.constructease.model.ItemPedido;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.model.StatusPedido;
import br.com.constructease.constructease.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    private PedidoRepository pedidoRepository;
    private EstoqueService estoqueService;
    private PedidoService pedidoService;

    @BeforeEach
    void setup() throws Exception {
        pedidoRepository = mock(PedidoRepository.class);
        estoqueService = mock(EstoqueService.class);
        pedidoService = new PedidoService();

        // Injetar pedidoRepository via reflexão
        Field pedidoRepoField = PedidoService.class.getDeclaredField("pedidoRepository");
        pedidoRepoField.setAccessible(true);
        pedidoRepoField.set(pedidoService, pedidoRepository);

        // Injetar estoqueService via reflexão
        Field estoqueField = PedidoService.class.getDeclaredField("estoqueService");
        estoqueField.setAccessible(true);
        estoqueField.set(pedidoService, estoqueService);
    }

    @Test
    void criarPedido_deveSalvarPedidoComItens() {
        PedidoDTO dto = new PedidoDTO("Pedido Teste", List.of(new ItemPedidoDTO(1, 2)));
        when(estoqueService.getPrecoProduto(1)).thenReturn(100.0);
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Pedido pedido = pedidoService.criarPedido(dto);
        assertEquals(1, pedido.getItens().size());
        assertEquals(StatusPedido.ATIVO, pedido.getStatus());
    }

    @Test
    void cancelarPedido_deveAlterarStatusParaCancelado() {
        Pedido pedido = new Pedido("Teste"); pedido.setId(1L); pedido.setStatus(StatusPedido.ATIVO);
        when(pedidoRepository.findOptionalById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.cancelarPedido(1L);
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
    }

    @Test
    void buscarPedidoObrigatorio_deveLancarExcecaoSeNaoEncontrado() {
        when(pedidoRepository.findOptionalById(99L)).thenReturn(Optional.empty());
        assertThrows(PedidoNaoEncontradoException.class, () -> pedidoService.buscarPedidoObrigatorio(99L));
    }
}
