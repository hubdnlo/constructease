//package br.com.constructease.constructease.service;
//
//import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
//import br.com.constructease.constructease.exception.EstoqueInsuficienteException;
//import br.com.constructease.constructease.exception.ProdutoInexistenteException;
//import br.com.constructease.constructease.model.Produto;
//import br.com.constructease.constructease.repository.EstoqueRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class EstoqueServiceTest {
//
//    @Mock
//    private EstoqueRepository estoqueRepository;
//
//    @InjectMocks
//    private EstoqueService estoqueService;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void listarItensDisponiveis_deveRetornarApenasProdutosComEstoque() {
//        Produto p1 = new Produto(); p1.setQuantidadeEstoque(10);
//        Produto p2 = new Produto(); p2.setQuantidadeEstoque(0);
//
//        when(estoqueRepository.buscarTodos()).thenReturn(List.of(p1, p2));
//
//        List<Produto> result = estoqueService.listarItensDisponiveis();
//        assertEquals(1, result.size());
//        assertTrue(result.contains(p1));
//        assertFalse(result.contains(p2));
//    }
//
//    @Test
//    void getPrecoProduto_deveLancarExcecaoSeSemEstoque() {
//        Produto produto = new Produto(); produto.setQuantidadeEstoque(0); produto.setPreco(100.0);
//
//        when(estoqueRepository.buscarPorId(1)).thenReturn(Optional.of(produto));
//
//        assertThrows(EstoqueInsuficienteException.class, () -> estoqueService.getPrecoProduto(1));
//    }
//
//    @Test
//    void getPrecoProduto_deveLancarExcecaoSeProdutoInexistente() {
//        when(estoqueRepository.buscarPorId(99)).thenReturn(Optional.empty());
//
//        assertThrows(ProdutoInexistenteException.class, () -> estoqueService.getPrecoProduto(99));
//    }
//
//    @Test
//    void cadastrarOuAtualizarProduto_deveAtualizarPrecoPonderado() {
//        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Cimento", 10, 50.0);
//        Produto existente = new Produto();
//        existente.setId(1);
//        existente.setNome("Cimento");
//        existente.setQuantidadeEstoque(10);
//        existente.setPreco(40.0);
//
//        when(estoqueRepository.buscarTodos()).thenReturn(new ArrayList<>(List.of(existente)));
//
//        estoqueService.cadastrarOuAtualizarProduto(dto);
//
//        verify(estoqueRepository).atualizarProduto(argThat(produto ->
//                produto.getNome().equals("Cimento") &&
//                        produto.getQuantidadeEstoque() == 20 &&
//                        Math.abs(produto.getPreco() - 45.0) < 0.01
//        ));
//    }
//
//    @Test
//    void cadastrarOuAtualizarProduto_deveCadastrarNovoProdutoSeNaoExistente() {
//        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Areia", 5, 20.0);
//
//        when(estoqueRepository.buscarTodos()).thenReturn(new ArrayList<>());
//
//        estoqueService.cadastrarOuAtualizarProduto(dto);
//
//        verify(estoqueRepository).atualizarEstoque(argThat(produtos ->
//                produtos.stream().anyMatch(p ->
//                        p.getNome().equals("Areia") &&
//                                p.getQuantidadeEstoque() == 5 &&
//                                p.getPreco() == 20.0
//                )
//        ));
//    }
//}
