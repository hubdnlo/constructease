package br.com.constructease.repository;

import br.com.constructease.exception.PedidoNaoEncontradoException;
import br.com.constructease.exception.PersistenciaPedidoException;
import br.com.constructease.model.Pedido;
import br.com.constructease.model.StatusPedido;
import br.com.constructease.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes unitários para PedidoRepository")
class PedidoRepositoryTest {

    @Mock
    private Resource pedidoJson;

    @InjectMocks
    private PedidoRepository repository;

    private Pedido pedido;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        pedido = new Pedido(1L, "Pedido de teste", new ArrayList<>());
        when(pedidoJson.getInputStream()).thenReturn(new ByteArrayInputStream("[]".getBytes()));
        when(pedidoJson.getFile()).thenReturn(new File("mocked-path.json"));
        mockStatic(JsonUtil.class);
    }

    @AfterEach
    void tearDown() {
        clearAllCaches();
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao falhar leitura do arquivo")
    void buscarTodosComFalhaDeLeitura() throws IOException {
        when(pedidoJson.getInputStream()).thenThrow(new IOException("Erro simulado"));

        List<Pedido> pedidos = repository.buscarTodos();

        assertTrue(pedidos.isEmpty());
    }

    @Test
    @DisplayName("Deve cancelar pedido existente e atualizar status")
    void cancelarPedidoExistente() {
        pedido.setStatus(StatusPedido.ATIVO);
        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(List.of(pedido));

        doNothing().when(JsonUtil.class);
        JsonUtil.gravarJson(anyString(), anyList());

        repository.cancelar(1);

        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar pedido inexistente")
    void cancelarPedidoInexistente() {
        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(List.of());

        assertThrows(PedidoNaoEncontradoException.class, () -> repository.cancelar(99));
    }

    @Test
    @DisplayName("Deve salvar novo pedido quando ID é nulo")
    void salvarNovoPedido() throws Exception {
        Pedido novo = new Pedido(null, "Novo pedido", new ArrayList<>());

        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(new ArrayList<>());

        doNothing().when(JsonUtil.class);
        JsonUtil.gravarJson(anyString(), anyList());

        Pedido resultado = repository.save(novo);

        assertNotNull(resultado);
        assertEquals("Novo pedido", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve atualizar pedido existente")
    void atualizarPedidoExistente() {
        Pedido existente = new Pedido(1L, "Pedido original", new ArrayList<>());
        Pedido atualizado = new Pedido(1L, "Pedido atualizado", new ArrayList<>());

        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(new ArrayList<>(List.of(existente)));

        doNothing().when(JsonUtil.class);
        JsonUtil.gravarJson(anyString(), anyList());

        Pedido resultado = repository.save(atualizado);

        assertEquals("Pedido atualizado", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar pedido inexistente")
    void atualizarPedidoInexistente() {
        Pedido pedido = new Pedido(99L, "Pedido inexistente", new ArrayList<>());

        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(new ArrayList<>());

        assertThrows(PedidoNaoEncontradoException.class, () -> repository.save(pedido));
    }

    @Test
    @DisplayName("Deve lançar PersistenciaPedidoException ao falhar na gravação de pedidos")
    void gravarPedidosComFalha() throws Exception {
        when(pedidoJson.getFile()).thenThrow(new IOException("Falha simulada"));

        Method gravar = PedidoRepository.class.getDeclaredMethod("gravarPedidos", List.class);
        gravar.setAccessible(true);

        List<Pedido> pedidos = List.of(new Pedido(1L, "Teste", new ArrayList<>()));

        Exception exception = assertThrows(Exception.class, () -> gravar.invoke(repository, pedidos));

        // Verifica se a causa da InvocationTargetException é PersistenciaPedidoException
        assertTrue(exception.getCause() instanceof PersistenciaPedidoException);
        assertEquals("Falha ao gravar pedidos no arquivo.", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Deve encontrar pedido por ID existente")
    void findOptionalByIdExistente() {
        Pedido pedido = new Pedido(1L, "Pedido", new ArrayList<>());
        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(List.of(pedido));

        Optional<Pedido> resultado = repository.findOptionalById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Pedido", resultado.get().getDescricao());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar pedido por ID inexistente")
    void findOptionalByIdInexistente() {
        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(List.of());

        Optional<Pedido> resultado = repository.findOptionalById(99L);

        assertTrue(resultado.isEmpty());
    }
}