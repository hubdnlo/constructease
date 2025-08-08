package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueRepositoryTest {

    private EstoqueRepository estoqueRepository;
    private File estoqueJson;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        estoqueJson = tempDir.resolve("estoque.json").toFile();
        estoqueRepository = new EstoqueRepository(estoqueJson.getAbsolutePath());

        Produto produto = new Produto();
        produto.setId(2);
        produto.setNome("Areia Média");
        produto.setPreco(15.0);
        produto.setQuantidadeEstoque(100);

        estoqueRepository.atualizarEstoque(List.of(produto));
    }

    @Test
    void deveAtualizarProdutoNoJson() {
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(2);
        produtoAtualizado.setNome("Areia Média Atualizada");
        produtoAtualizado.setPreco(17.90);
        produtoAtualizado.setQuantidadeEstoque(300);

        estoqueRepository.atualizarProduto(produtoAtualizado);

        Optional<Produto> resultado = estoqueRepository.buscarPorId(2);
        assertTrue(resultado.isPresent(), "Produto deveria estar presente");
        assertEquals("Areia Média Atualizada", resultado.get().getNome());
        assertEquals(300, resultado.get().getQuantidadeEstoque());
        assertEquals(17.90, resultado.get().getPreco());
    }
}
