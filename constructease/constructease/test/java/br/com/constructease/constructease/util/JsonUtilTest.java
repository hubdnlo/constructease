package br.com.constructease.constructease.util;

import br.com.constructease.constructease.exception.JsonGravacaoException;
import br.com.constructease.constructease.exception.JsonLeituraException;
import br.com.constructease.constructease.model.Produto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;

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
    @DisplayName("Deve lançar exceção ao ler JSON inválido")
    void lerJsonInvalido() throws IOException {
        Files.writeString(Paths.get(TEST_PATH), "conteúdo inválido");

        assertThrows(JsonLeituraException.class, () ->
                JsonUtil.lerJson(TEST_PATH, new TypeReference<List<Produto>>() {}));
    }

    @Test
    @DisplayName("Deve lançar exceção ao gravar em caminho inválido simulando diretório")
    void gravarJsonEmArquivoComoDiretorio() throws IOException {
        Path fakeDir = Paths.get("data/fake-as-dir.json");
        Files.createDirectories(Paths.get("data"));
        Files.deleteIfExists(fakeDir);
        Files.createFile(fakeDir); // cria como arquivo

        String caminhoInvalido = "data/fake-as-dir.json/teste.json"; // tenta gravar dentro de um arquivo

        List<Produto> lista = List.of(new Produto("Cimento", "CP-II", 1, 10, new BigDecimal("25.00")));

        assertThrows(JsonGravacaoException.class, () ->
                JsonUtil.gravarJson(caminhoInvalido, lista));
    }
}