package br.com.constructease.model.factory;

import br.com.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.model.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes unitários para ProdutoFactory")
class ProdutoFactoryTest {

    @Test
    @DisplayName("Deve criar produto com ID incremental baseado na lista existente")
    void criarProdutoComIdIncremental() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Cimento", "CP-II", 1, new BigDecimal("25.00"),1);

        Produto existente = new Produto("Areia", "Fina", 2, 5, new BigDecimal("10.00"));
        existente.setId(5L);

        Produto novo = ProdutoFactory.criar(dto, List.of(existente));

        assertEquals(6L, novo.getId());
        assertEquals("Cimento", novo.getNome());
        assertEquals("CP-II", novo.getDescricao());
        assertEquals(1, novo.getCategoriaId());
        assertEquals(1, novo.getQuantidade());
        assertEquals(new BigDecimal("25.00"), novo.getPreco());
    }

    @Test
    @DisplayName("Deve criar produto com ID 1 quando lista está vazia")
    void criarProdutoComListaVazia() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Tijolo","Cerâmico",3, new BigDecimal("0.50"), 1);

        Produto novo = ProdutoFactory.criar(dto, List.of());

        assertEquals(1L, novo.getId());
    }
}