package br.com.constructease.constructease.repository;

//import br.com.constructease.constructease.model.Estoque;
import br.com.constructease.constructease.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EstoqueRepositoryTest {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Test
    void deveAtualizarProdutoNoJson() {
        Produto produto = new Produto();
        produto.setId(2);
        produto.setNome("Areia Média Atualizada");
        produto.setPreco(17.90);
        produto.setQuantidadeEstoque(300);

        estoqueRepository.atualizarProduto(produto);

        Optional<Produto> atualizado = estoqueRepository.buscarPorId(2);
        assertTrue(atualizado.isPresent());
        assertEquals("Areia Média Atualizada", atualizado.get().getNome());
        assertEquals(300, atualizado.get().getQuantidadeEstoque());
    }

}

