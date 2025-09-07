package br.com.constructease.constructease.util;

import br.com.constructease.constructease.exception.JsonGravacaoException;
import br.com.constructease.constructease.exception.JsonLeituraException;
import br.com.constructease.constructease.model.Produto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para JsonUtil")
class JsonUtilTest {

    private static final String TEST_PATH = "data/test-jsonutil.json";

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(Paths.get("data"));
        Files.deleteIfExists(Paths.get(TEST_PATH));
    }

    @Test
    @DisplayName("Deve gravar lista de objetos em arquivo JSON")
    void gravarJsonComSucesso() {
        Produto produto = new Produto("Cimento", "CP-II", 1, 10, new BigDecimal("25.00"));
        produto.setId(1L);
        List<Produto> lista = List.of(produto);

        JsonUtil.gravarJson(TEST_PATH, lista);

        assertTrue(Files.exists(Paths.get(TEST_PATH)));
    }

    @Test
    @DisplayName("Deve ler lista de objetos de arquivo JSON")
    void lerListaComSucesso() {
        Produto produto = new Produto("Cimento", "CP-II", 1, 10, new BigDecimal("25.00"));
        produto.setId(1L);
        JsonUtil.gravarJson(TEST_PATH, List.of(produto));

        List<Produto> resultado = JsonUtil.lerLista(TEST_PATH, Produto[].class);

        assertEquals(1, resultado.size());
        assertEquals("Cimento", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao ler JSON inválido via caminho")
    void lerJsonViaCaminhoComErro() throws IOException {
        Files.writeString(Paths.get(TEST_PATH), "inválido");

        assertThrows(JsonLeituraException.class, () ->
                JsonUtil.lerJson(TEST_PATH, new TypeReference<List<Produto>>() {}));
    }

    @Test
    @DisplayName("Deve lançar exceção ao ler JSON inválido via InputStream")
    void lerJsonViaInputStreamComErro() {
        InputStream input = new ByteArrayInputStream("inválido".getBytes());

        assertThrows(JsonLeituraException.class, () ->
                JsonUtil.lerJson(input, new TypeReference<List<Produto>>() {}));
    }

    @Test
    @DisplayName("Deve lançar exceção ao gravar em caminho inválido simulando diretório")
    void gravarJsonEmArquivoComoDiretorio() throws IOException {
        Path fakeDir = Paths.get("data/fake-as-dir.json");
        Files.deleteIfExists(fakeDir);
        Files.createFile(fakeDir); // cria como arquivo

        String caminhoInvalido = "data/fake-as-dir.json/teste.json";

        List<Produto> lista = List.of(new Produto("Cimento", "CP-II", 1, 10, new BigDecimal("25.00")));

        assertThrows(JsonGravacaoException.class, () ->
                JsonUtil.gravarJson(caminhoInvalido, lista));
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao falhar leitura em lerLista")
    void lerListaComErroDeFormato() throws IOException {
        Files.writeString(Paths.get(TEST_PATH), "inválido");

        List<Produto> resultado = JsonUtil.lerLista(TEST_PATH, Produto[].class);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar JsonLeituraException ao abrir recurso inexistente")
    void abrirArquivoRecursoInexistente() {
        String caminhoInexistente = "recurso-nao-existe.json";

        assertThrows(JsonLeituraException.class, () ->
                JsonUtil.lerJson(caminhoInexistente, new TypeReference<List<Produto>>() {}));
    }

    @Test
    @DisplayName("Deve ler JSON via InputStream com sucesso")
    void lerJsonViaInputStreamComSucesso() throws IOException {
        Produto produto = new Produto("Areia", "Fina", 2, 5, new BigDecimal("10.00"));
        produto.setId(2L);
        JsonUtil.gravarJson(TEST_PATH, List.of(produto));

        try (InputStream input = Files.newInputStream(Paths.get(TEST_PATH))) {
            List<Produto> resultado = JsonUtil.lerJson(input, new TypeReference<List<Produto>>() {});
            assertEquals(1, resultado.size());
            assertEquals("Areia", resultado.get(0).getNome());
        }
    }

    @Test
    @DisplayName("Deve lançar FileNotFoundException ao abrir arquivo inexistente e recurso ausente")
    void abrirArquivoInexistente() {
        String caminhoInvalido = "data/arquivo-nao-existe.json";
        assertThrows(JsonLeituraException.class, () ->
                JsonUtil.lerJson(caminhoInvalido, new TypeReference<List<Produto>>() {}));
    }

    @Test
    @DisplayName("Deve criar diretório se não existir usando reflexão")
    void criarDiretorioSeNecessario() {
        Path novoCaminho = Paths.get("data/teste/subdir/teste.json");
        try {
            Files.deleteIfExists(novoCaminho.getParent());

            Method metodo = JsonUtil.class.getDeclaredMethod("criarDiretorioSeNecessario", Path.class);
            metodo.setAccessible(true);
            metodo.invoke(null, novoCaminho);

            assertTrue(Files.exists(novoCaminho.getParent()));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            fail("Erro ao acessar método privado via reflexão: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve acessar construtor privado de JsonUtil via reflexão")
    void construtorPrivadoViaReflexao() throws Exception {
        Constructor<JsonUtil> construtor = JsonUtil.class.getDeclaredConstructor();
        construtor.setAccessible(true);
        JsonUtil instancia = construtor.newInstance();
        assertNotNull(instancia);
    }
}