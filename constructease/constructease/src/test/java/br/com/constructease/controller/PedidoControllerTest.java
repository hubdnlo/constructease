package br.com.constructease.controller;

import br.com.constructease.dto.ItemPedidoDTO;
import br.com.constructease.dto.PedidoDTO;
import br.com.constructease.dto.PedidoResponseDTO;
import br.com.constructease.model.Pedido;
import br.com.constructease.model.StatusPedido;
import br.com.constructease.service.EstoqueService;
import br.com.constructease.service.PedidoService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes unitários para PedidoController")
class PedidoControllerTest {

    @InjectMocks
    private PedidoController controller;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private EstoqueService estoqueService;

    private Pedido pedido;
    private PedidoResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        pedido = new Pedido(1L, "Pedido de teste", new ArrayList<>());

        List<ItemPedidoDTO> itensDTO = List.of(
                new ItemPedidoDTO(1L, "Cimento", 2, new BigDecimal("50.00"))
        );

        responseDTO = new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDescricao(),
                pedido.getStatus().name(),
                new BigDecimal("100.00"),
                itensDTO
        );
    }

    @Test
    @DisplayName("Deve criar um novo pedido e retornar status 201 com PedidoResponseDTO")
    void criarPedidoComSucesso() {
        PedidoDTO dto = new PedidoDTO();
        dto.setDescricao("Pedido de teste");

        when(pedidoService.criarPedido(dto)).thenReturn(pedido);
        when(pedidoService.gerarPedidoResponseDTO(pedido)).thenReturn(responseDTO);

        ResponseEntity<PedidoResponseDTO> response = controller.criar(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDTO.getDescricao(), response.getBody().getDescricao());
        verify(pedidoService).criarPedido(dto);
        verify(pedidoService).gerarPedidoResponseDTO(pedido);
    }

    @Test
    @DisplayName("Deve cancelar pedido existente e retornar status 204")
    void cancelarPedidoComSucesso() {
        doNothing().when(pedidoService).cancelarPedido(1L);

        ResponseEntity<Void> response = controller.cancelar(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(pedidoService).cancelarPedido(1L);
    }

    @Test
    @DisplayName("Deve listar todos os pedidos quando status não é informado")
    void listarTodosPedidos() {
        when(pedidoService.listarTodos()).thenReturn(List.of(pedido));
        when(pedidoService.gerarPedidoResponseDTO(pedido)).thenReturn(responseDTO);

        ResponseEntity<List<PedidoResponseDTO>> response = controller.listar(null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Pedido de teste", response.getBody().get(0).getDescricao());
        verify(pedidoService).listarTodos();
        verify(pedidoService).gerarPedidoResponseDTO(pedido);
    }

    @Test
    @DisplayName("Deve listar pedidos filtrando por status")
    void listarPedidosPorStatus() {
        when(pedidoService.listarPorStatus(StatusPedido.ATIVO)).thenReturn(List.of(pedido));
        when(pedidoService.gerarPedidoResponseDTO(pedido)).thenReturn(responseDTO);

        ResponseEntity<List<PedidoResponseDTO>> response = controller.listar(StatusPedido.ATIVO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(StatusPedido.ATIVO.name(), response.getBody().get(0).getStatus());
        verify(pedidoService).listarPorStatus(StatusPedido.ATIVO);
        verify(pedidoService).gerarPedidoResponseDTO(pedido);
    }
}