package br.com.constructease.service;

import br.com.constructease.dto.ItemPedidoDTO;
import br.com.constructease.dto.PedidoDTO;
import br.com.constructease.dto.PedidoResponseDTO;
import br.com.constructease.exception.PedidoNaoEncontradoException;
import br.com.constructease.model.ItemPedido;
import br.com.constructease.model.Pedido;
import br.com.constructease.model.StatusPedido;
import br.com.constructease.repository.PedidoRepository;
import br.com.constructease.util.JsonUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes unitários para PedidoService")
class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private EstoqueService estoqueService;

    private Pedido pedido;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        pedido = new Pedido("Pedido de teste");
        pedido.setId(1L);
        pedido.setStatus(StatusPedido.ATIVO);
        pedido.adicionarItem(new ItemPedido(1L, 2, new BigDecimal("10.00")));
    }

    @Test
    @DisplayName("Deve criar pedido com itens válidos e estoque disponível")
    void criarPedidoComSucesso() {
        ItemPedidoDTO itemDTO = new ItemPedidoDTO(1L, 2);
        PedidoDTO dto = new PedidoDTO("Novo pedido", List.of(itemDTO));

        when(estoqueService.isDisponivel(1L, 2)).thenReturn(true);
        when(estoqueService.getPrecoProduto(1L)).thenReturn(new BigDecimal("10.00"));
        when(pedidoRepository.save(any())).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });

        try (MockedStatic<JsonUtil> jsonMock = mockStatic(JsonUtil.class)) {
            jsonMock.when(() -> JsonUtil.lerLista(anyString(), eq(Pedido[].class))).thenReturn(new ArrayList<>());
            jsonMock.when(() -> JsonUtil.lerLista(eq("data/pedidos_temp.json"), eq(Pedido[].class))).thenReturn(List.of(pedido));
            jsonMock.when(() -> JsonUtil.gravarJson(anyString(), anyList())).thenAnswer(inv -> null);

            Pedido resultado = pedidoService.criarPedido(dto);

            assertEquals("Novo pedido", resultado.getDescricao());
            assertEquals(1, resultado.getItens().size());
            assertEquals(new BigDecimal("20.00"), resultado.getValorTotal());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido com estoque insuficiente")
    void criarPedidoComEstoqueInsuficiente() {
        ItemPedidoDTO itemDTO = new ItemPedidoDTO(1L, 5);
        PedidoDTO dto = new PedidoDTO("Pedido inválido", List.of(itemDTO));

        when(estoqueService.isDisponivel(1L, 5)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> pedidoService.criarPedido(dto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido com DTO nulo")
    void criarPedidoComDTONulo() {
        assertThrows(IllegalArgumentException.class, () -> pedidoService.criarPedido(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido com lista de itens nula")
    void criarPedidoComItensNulos() {
        PedidoDTO dto = new PedidoDTO("Pedido inválido", null);
        assertThrows(IllegalArgumentException.class, () -> pedidoService.criarPedido(dto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido com lista de itens vazia")
    void criarPedidoComItensVazios() {
        PedidoDTO dto = new PedidoDTO("Pedido inválido", List.of());
        assertThrows(IllegalArgumentException.class, () -> pedidoService.criarPedido(dto));
    }

    @Test
    @DisplayName("Deve calcular corretamente o valor total do pedido")
    void calcularTotalPedido() {
        BigDecimal total = pedidoService.calcularTotalPedido(pedido);
        assertEquals(new BigDecimal("20.00"), total);
    }

    @Test
    @DisplayName("Deve retornar zero ao calcular total de pedido sem itens")
    void calcularTotalPedidoSemItens() {
        Pedido vazio = new Pedido("Pedido vazio");
        vazio.setId(2L);
        BigDecimal total = pedidoService.calcularTotalPedido(vazio);
        assertTrue(total.compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    @DisplayName("Deve cancelar pedido ativo e devolver itens ao estoque")
    void cancelarPedidoAtivo() {
        when(pedidoRepository.findOptionalById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedido);

        pedidoService.cancelarPedido(1L);

        verify(estoqueService).devolverEstoque(1L, 2);
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar pedido já cancelado")
    void cancelarPedidoJaCancelado() {
        pedido.setStatus(StatusPedido.CANCELADO);
        when(pedidoRepository.findOptionalById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(IllegalStateException.class, () -> pedidoService.cancelarPedido(1L));
    }

    @Test
    @DisplayName("Deve listar todos os pedidos")
    void listarTodos() {
        when(pedidoRepository.buscarTodos()).thenReturn(List.of(pedido));
        List<Pedido> pedidos = pedidoService.listarTodos();
        assertEquals(1, pedidos.size());
    }

    @Test
    @DisplayName("Deve listar apenas pedidos com status ATIVO")
    void listarPedidosAtivos() {
        Pedido cancelado = new Pedido("Cancelado");
        cancelado.setId(2L);
        cancelado.setStatus(StatusPedido.CANCELADO);

        when(pedidoRepository.buscarTodos()).thenReturn(List.of(pedido, cancelado));
        List<Pedido> ativos = pedidoService.listarPedidosAtivos();
        assertEquals(1, ativos.size());
        assertEquals(StatusPedido.ATIVO, ativos.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve listar pedidos por status")
    void listarPorStatus() {
        when(pedidoRepository.buscarTodos()).thenReturn(List.of(pedido));
        List<Pedido> resultado = pedidoService.listarPorStatus(StatusPedido.ATIVO);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Deve buscar pedido obrigatório existente")
    void buscarPedidoObrigatorioExistente() {
        when(pedidoRepository.findOptionalById(1L)).thenReturn(Optional.of(pedido));
        Pedido resultado = pedidoService.buscarPedidoObrigatorio(1L);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar pedido inexistente")
    void buscarPedidoObrigatorioInexistente() {
        when(pedidoRepository.findOptionalById(99L)).thenReturn(Optional.empty());
        assertThrows(PedidoNaoEncontradoException.class, () -> pedidoService.buscarPedidoObrigatorio(99L));
    }

    @Test
    @DisplayName("Deve gerar DTO de resposta com valor total calculado")
    void gerarPedidoResponseDTO() {
        when(estoqueService.getNomeProduto(1L)).thenReturn("Cimento");
        PedidoResponseDTO dto = pedidoService.gerarPedidoResponseDTO(pedido);
        assertEquals(new BigDecimal("20.00"), dto.getValorTotal());
        assertEquals("Cimento", dto.getItens().get(0).getNomeProduto());
    }

    @Test
    @DisplayName("Deve capturar exceção ao gravar JSON e continuar execução")
    void criarPedidoComFalhaNaGravacaoJson() {
        ItemPedidoDTO itemDTO = new ItemPedidoDTO(1L, 2);
        PedidoDTO dto = new PedidoDTO("Pedido com erro", List.of(itemDTO));

        when(estoqueService.isDisponivel(1L, 2)).thenReturn(true);
        when(estoqueService.getPrecoProduto(1L)).thenReturn(new BigDecimal("10.00"));
        when(pedidoRepository.save(any())).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });

        try (MockedStatic<JsonUtil> jsonMock = mockStatic(JsonUtil.class)) {
            jsonMock.when(() -> JsonUtil.lerLista(anyString(), eq(Pedido[].class)))
                    .thenThrow(new RuntimeException("Erro simulado"));

            Pedido resultado = pedidoService.criarPedido(dto);
            assertEquals("Pedido com erro", resultado.getDescricao());
        }
    }

    @Test
    @DisplayName("Deve evitar duplicidade ao gravar pedido já existente no JSON")
    void criarPedidoJaExistenteNoJson() {
        ItemPedidoDTO itemDTO = new ItemPedidoDTO(1L, 2);
        PedidoDTO dto = new PedidoDTO("Pedido duplicado", List.of(itemDTO));

        when(estoqueService.isDisponivel(1L, 2)).thenReturn(true);
        when(estoqueService.getPrecoProduto(1L)).thenReturn(new BigDecimal("10.00"));
        when(pedidoRepository.save(any())).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        try (MockedStatic<JsonUtil> jsonMock = mockStatic(JsonUtil.class)) {
            jsonMock.when(() -> JsonUtil.lerLista(anyString(), eq(Pedido[].class)))
                    .thenReturn(List.of(pedido));
            jsonMock.when(() -> JsonUtil.gravarJson(anyString(), anyList()))
                    .thenAnswer(inv -> null);
            jsonMock.when(() -> JsonUtil.lerLista(eq("data/pedidos_temp.json"), eq(Pedido[].class)))
                    .thenReturn(List.of(pedido));

            Pedido resultado = pedidoService.criarPedido(dto);
            assertEquals("Pedido duplicado", resultado.getDescricao());
        }
    }

    @Test
    @DisplayName("Deve não mover arquivo se lista de validação estiver vazia")
    void criarPedidoComValidacaoVazia() {
        ItemPedidoDTO itemDTO = new ItemPedidoDTO(1L, 2);
        PedidoDTO dto = new PedidoDTO("Pedido inválido", List.of(itemDTO));

        when(estoqueService.isDisponivel(1L, 2)).thenReturn(true);
        when(estoqueService.getPrecoProduto(1L)).thenReturn(new BigDecimal("10.00"));
        when(pedidoRepository.save(any())).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });

        try (MockedStatic<JsonUtil> jsonMock = mockStatic(JsonUtil.class)) {
            jsonMock.when(() -> JsonUtil.lerLista(anyString(), eq(Pedido[].class)))
                    .thenReturn(new ArrayList<>());
            jsonMock.when(() -> JsonUtil.gravarJson(anyString(), anyList()))
                    .thenAnswer(inv -> null);
            jsonMock.when(() -> JsonUtil.lerLista(eq("data/pedidos_temp.json"), eq(Pedido[].class)))
                    .thenReturn(new ArrayList<>());

            Pedido resultado = pedidoService.criarPedido(dto);
            assertEquals("Pedido inválido", resultado.getDescricao());
        }
    }
}