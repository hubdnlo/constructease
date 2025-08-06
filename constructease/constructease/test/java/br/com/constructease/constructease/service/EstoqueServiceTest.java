package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.exception.EstoqueInsuficienteException;
import br.com.constructease.constructease.exception.ProdutoInexistenteException;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.repository.EstoqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueServiceTest {

    private EstoqueRepository estoqueRepository;
    private EstoqueService estoqueService;

    @BeforeEach
    void setup() throws Exception {
        estoqueRepository = mock(EstoqueRepository.class);
        estoqueService = new EstoqueService();

        Field field = EstoqueService.class.getDeclaredField("estoqueRepository");
        field.setAccessible(true);
        field.set(estoqueService, estoqueRepository);
    }

    @Test
    void listarItensDisponiveis_deveRetornarProdutosComEstoque() {
        Produto p1 = new Produto(); p1.setQuantidadeEstoque(10);
        Produto p2 = new Produto(); p2.setQuantidadeEstoque(0);
        when(estoqueRepository.buscarTodos()).thenReturn(List.of(p1, p2));

        List<Produto> result = estoqueService.listarItensDisponiveis();
        assertEquals(1, result.size());
    }

    @Test
    void getPrecoProduto_deveLancarExcecaoSeSemEstoque() {
        Produto p = new Produto(); p.setQuantidadeEstoque(0); p.setPreco(100);
        when(estoqueRepository.buscarPorId(1)).thenReturn(Optional.of(p));

        assertThrows(EstoqueInsuficienteException.class, () -> estoqueService.getPrecoProduto(1));
    }

    @Test
    void cadastrarOuAtualizarProduto_deveAtualizarPrecoPonderado() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Cimento", 10, 50.0);
        Produto existente = new Produto(); existente.setNome("Cimento"); existente.setQuantidadeEstoque(10); existente.setPreco(40.0);

        when(estoqueRepository.buscarTodos()).thenReturn(List.of(existente));

        estoqueService.cadastrarOuAtualizarProduto(dto);
        verify(estoqueRepository).atualizarProduto(any());
    }
}
