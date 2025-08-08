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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private EstoqueService estoqueService;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarPedido_deveSalvarPedidoComItens() {
        PedidoDTO dto = new PedidoDTO("Pedido Teste", List.of(new ItemPedidoDTO(1, 2)));
        when(estoqueService.getPrecoProduto(1)).thenReturn(100.0);
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Pedido pedido = pedidoService.criarPedido(dto);

        assertEquals("Pedido Teste", pedido.getDescricao());
        assertEquals(1, pedido.getItens().size());
        assertEquals(StatusPedido.ATIVO, pedido.getStatus());

        ItemPedido item = pedido.getItens().get(0);
        assertEquals(1, item.getProdutoId());
        assertEquals(2, item.getQuantidade());
        assertEquals(100.0, item.getPrecoUnitario());
    }

    @Test
    void cancelarPedido_deveAlterarStatusParaCancelado() {
        Pedido pedido = new Pedido("Teste");
        pedido.setId(1L);
        pedido.setStatus(StatusPedido.ATIVO);

        when(pedidoRepository.findOptionalById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.cancelarPedido(1L);

        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void buscarPedidoObrigatorio_deveLancarExcecaoSeNaoEncontrado() {
        when(pedidoRepository.findOptionalById(99L)).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class, () -> pedidoService.buscarPedidoObrigatorio(99L));
    }

    @Test
    void criarPedido_comMultiplosItens_deveCalcularCorretamente() {
        PedidoDTO dto = new PedidoDTO("Pedido Múltiplos Itens", List.of(
                new ItemPedidoDTO(1, 2),
                new ItemPedidoDTO(2, 3)
        ));

        when(estoqueService.getPrecoProduto(1)).thenReturn(50.0);
        when(estoqueService.getPrecoProduto(2)).thenReturn(30.0);
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Pedido pedido = pedidoService.criarPedido(dto);

        assertEquals(2, pedido.getItens().size());
        assertEquals(50.0, pedido.getItens().get(0).getPrecoUnitario());
        assertEquals(30.0, pedido.getItens().get(1).getPrecoUnitario());
    }
}
