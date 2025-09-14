package br.com.constructease.repository;

import br.com.constructease.exception.EstoqueNaoEncontradoException;
import br.com.constructease.exception.PersistenciaEstoqueException;
import br.com.constructease.model.Produto;
import br.com.constructease.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.core.io.Resource;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes unitários para EstoqueArquivoRepository")
class EstoqueArquivoRepositoryTest {

    @Mock
    private Resource estoqueJson;

    @InjectMocks
    private EstoqueArquivoRepository repository;

    private Produto produto;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        produto = new Produto("Cimento", "CP-II", 1, 10, new BigDecimal("25.00"));
        produto.setId(1L);

        when(estoqueJson.getInputStream()).thenReturn(new ByteArrayInputStream("[]".getBytes()));
        when(estoqueJson.getFile()).thenReturn(new File("mocked-path.json"));

        mockStatic(JsonUtil.class);
    }

    @AfterEach
    void tearDown() {
        clearAllCaches();
    }

    @Test
    @DisplayName("Deve retornar lista de produtos ao ler arquivo com sucesso")
    void buscarTodosComSucesso() {
        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(List.of(produto));

        List<Produto> resultado = repository.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Cimento", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Deve lançar EstoqueNaoEncontradoException ao falhar leitura do arquivo")
    void buscarTodosComFalhaDeLeitura() throws IOException {
        when(estoqueJson.getInputStream()).thenThrow(new IOException("Falha simulada"));
        when(estoqueJson.getFilename()).thenReturn("estoque.json");

        assertThrows(EstoqueNaoEncontradoException.class, () -> repository.buscarTodos());
    }

    @Test
    @DisplayName("Deve retornar produto existente ao buscar por ID")
    void buscarPorIdExistente() {
        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(List.of(produto));

        Optional<Produto> resultado = repository.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Cimento", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve retornar Optional.empty ao buscar produto inexistente por ID")
    void buscarPorIdInexistente() {
        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(List.of());

        Optional<Produto> resultado = repository.buscarPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar estoque com lista de produtos")
    void atualizarEstoqueComSucesso() {
        doNothing().when(JsonUtil.class);
        JsonUtil.gravarJson(anyString(), anyList());

        List<Produto> produtos = List.of(produto);

        assertDoesNotThrow(() -> repository.atualizarEstoque(produtos));
    }

    @Test
    @DisplayName("Deve lançar PersistenciaEstoqueException ao falhar gravação de estoque")
    void atualizarEstoqueComFalha() throws IOException {
        when(estoqueJson.getFile()).thenThrow(new IOException("Falha simulada"));
        when(estoqueJson.getFilename()).thenReturn("estoque.json");

        List<Produto> produtos = List.of(produto);

        assertThrows(PersistenciaEstoqueException.class, () -> repository.atualizarEstoque(produtos));
    }

    @Test
    @DisplayName("Deve atualizar produto existente no estoque")
    void atualizarProdutoExistente() {
        Produto atualizado = new Produto("Cimento", "CP-II", 1, 20, new BigDecimal("30.00"));
        atualizado.setId(1L);

        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(new ArrayList<>(List.of(produto)));

        doNothing().when(JsonUtil.class);
        JsonUtil.gravarJson(anyString(), anyList());

        assertDoesNotThrow(() -> repository.atualizarProduto(atualizado));
    }

    @Test
    @DisplayName("Deve lançar EstoqueNaoEncontradoException ao tentar atualizar produto inexistente")
    void atualizarProdutoInexistente() {
        Produto inexistente = new Produto("Tijolo", "Cerâmico", 2, 100, new BigDecimal("0.50"));
        inexistente.setId(99L);

        when(JsonUtil.lerJson(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(new ArrayList<>(List.of(produto)));

        assertThrows(EstoqueNaoEncontradoException.class, () -> repository.atualizarProduto(inexistente));
    }
}