package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.service.EstoqueService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test") // Usa o application-test.properties
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes de integração para EstoqueArquivoRepository")
class EstoqueArquivoRepositoryIntegrationTest {

    private static final Path TEST_ESTOQUE_PATH = Paths.get("data/test-estoque.json");

    @Autowired
    private EstoqueArquivoRepository repository;

    @Autowired
    private EstoqueService estoqueService;

    @BeforeEach
    void prepararArquivoEstoque() throws IOException {
        Files.createDirectories(TEST_ESTOQUE_PATH.getParent());
        Files.writeString(TEST_ESTOQUE_PATH, "[]"); // Limpa o estoque antes de cada teste
    }

    @Test
    @Order(1)
    @DisplayName("Deve cadastrar novo produto via EstoqueService")
    void salvarProduto() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO(
                "Cimento", "CP-II", 50, new BigDecimal("25.00"), 1
        );

        estoqueService.cadastrarOuAtualizarProduto(dto);

        List<Produto> produtos = estoqueService.listarItensDisponiveis();
        assertTrue(produtos.stream().anyMatch(p -> p.getNome().equals("Cimento")));
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar todos os produtos salvos")
    void buscarTodosProdutos() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO(
                "Areia", "Fina", 20, new BigDecimal("10.00"), 2
        );

        estoqueService.cadastrarOuAtualizarProduto(dto);

        List<Produto> produtos = repository.buscarTodos();
        assertFalse(produtos.isEmpty());
        assertTrue(produtos.stream().anyMatch(p -> p.getNome().equals("Areia")));
    }
}