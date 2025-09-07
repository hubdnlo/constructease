package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.exception.EstoqueInsuficienteException;
import br.com.constructease.constructease.exception.PersistenciaEstoqueException;
import br.com.constructease.constructease.exception.ProdutoInexistenteException;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.repository.EstoqueArquivoRepository;
import br.com.constructease.constructease.util.FormatadorDecimal;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes unitários para EstoqueService")
class EstoqueServiceTest {

    @InjectMocks
    private EstoqueService estoqueService;

    @Mock
    private EstoqueArquivoRepository estoqueArquivoRepository;

    private Produto produto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        produto = new Produto("Cimento", "Cimento CP-II", 1, 50, new BigDecimal("25.00"));
        produto.setId(1L);
    }

    @Test
    @DisplayName("Deve listar apenas produtos com quantidade > 0")
    void listarItensDisponiveis() {
        Produto esgotado = new Produto("Areia", "Areia fina", 2, 0, new BigDecimal("10.00"));
        when(estoqueArquivoRepository.buscarTodos()).thenReturn(List.of(produto, esgotado));

        List<Produto> disponiveis = estoqueService.listarItensDisponiveis();

        assertEquals(1, disponiveis.size());
        assertEquals("Cimento", disponiveis.get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar produto existente ao consultar por ID")
    void consultarItemExistente() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        Produto resultado = estoqueService.consultarItem(1L);

        assertEquals("Cimento", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar produto inexistente")
    void consultarItemInexistente() {
        when(estoqueArquivoRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoInexistenteException.class, () -> estoqueService.consultarItem(99L));
    }

    @Test
    @DisplayName("Deve retornar preço arredondado de produto disponível")
    void getPrecoProdutoComEstoque() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        BigDecimal preco = estoqueService.getPrecoProduto(1L);

        assertEquals(new BigDecimal("25.00"), preco);
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter preço de produto sem estoque")
    void getPrecoProdutoSemEstoque() {
        produto.setQuantidade(0);
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        assertThrows(EstoqueInsuficienteException.class, () -> estoqueService.getPrecoProduto(1L));
    }

    @Test
    @DisplayName("Deve retornar nome do produto")
    void getNomeProduto() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        String nome = estoqueService.getNomeProduto(1L);

        assertEquals("Cimento", nome);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro em isDisponivel quando há estoque suficiente")
    void isDisponivelComEstoque() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        boolean disponivel = estoqueService.isDisponivel(1L, 10);

        assertTrue(disponivel);
    }

    @Test
    @DisplayName("Deve retornar falso em isDisponivel quando quantidade é insuficiente")
    void isDisponivelQuantidadeInsuficiente() {
        produto.setQuantidade(1);
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        boolean disponivel = estoqueService.isDisponivel(1L, 5);

        assertFalse(disponivel);
    }

    @Test
    @DisplayName("Deve lançar exceção em isDisponivel quando produto não existe")
    void isDisponivelProdutoInexistente() {
        when(estoqueArquivoRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoInexistenteException.class, () -> estoqueService.isDisponivel(99L, 1));
    }

    @Test
    @DisplayName("Deve baixar estoque corretamente")
    void baixarEstoqueComSucesso() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        estoqueService.baixarEstoque(1L, 10);

        verify(estoqueArquivoRepository).atualizarProduto(argThat(p -> p.getQuantidade() == 40));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar baixar estoque insuficiente")
    void baixarEstoqueInsuficiente() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        assertThrows(EstoqueInsuficienteException.class, () -> estoqueService.baixarEstoque(1L, 100));
    }

    @Test
    @DisplayName("Deve repor estoque corretamente")
    void reporEstoque() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        estoqueService.reporEstoque(1L, 20);

        verify(estoqueArquivoRepository).atualizarProduto(argThat(p -> p.getQuantidade() == 70));
    }

    @Test
    @DisplayName("Deve devolver estoque corretamente")
    void devolverEstoque() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        estoqueService.devolverEstoque(1L, 5);

        verify(estoqueArquivoRepository).atualizarProduto(argThat(p -> p.getQuantidade() == 55));
    }

    @Test
    @DisplayName("Deve atualizar nome do produto")
    void atualizarNomeProduto() {
        when(estoqueArquivoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        estoqueService.atualizarNomeProduto(1L, "Cimento Premium");

        verify(estoqueArquivoRepository).atualizarProduto(argThat(p -> p.getNome().equals("Cimento Premium")));
    }

    @Test
    @DisplayName("Deve cadastrar novo produto quando não existe")
    void cadastrarNovoProduto() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Novo", "Descrição", 3, new BigDecimal("10.00"), 3);

        when(estoqueArquivoRepository.buscarTodos()).thenReturn(new ArrayList<>());

        estoqueService.cadastrarOuAtualizarProduto(dto);

        verify(estoqueArquivoRepository).atualizarEstoque(argThat(list -> list.size() == 1));
    }

    @Test
    @DisplayName("Deve atualizar produto existente com preço ponderado")
    void atualizarProdutoExistente() {
        Produto existente = new Produto("Cimento", "Cimento CP-II", 1, 10, new BigDecimal("20.00"));
        existente.setId(1L);
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Cimento", "Cimento CP-II", 10, new BigDecimal("30.00"), 1);

        when(estoqueArquivoRepository.buscarTodos()).thenReturn(new ArrayList<>(List.of(existente)));

        estoqueService.cadastrarOuAtualizarProduto(dto);

        verify(estoqueArquivoRepository).atualizarProduto(argThat(p ->
                p.getQuantidade() == 20 &&
                        FormatadorDecimal.arredondar(p.getPreco()).equals(new BigDecimal("25.00"))
        ));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar produto com preço inválido")
    void cadastrarProdutoComPrecoInvalido() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Produto", "Descrição", 1, BigDecimal.ZERO, 1);
        assertThrows(IllegalArgumentException.class, () -> estoqueService.cadastrarOuAtualizarProduto(dto));
    }

    @Test
    @DisplayName("Deve falhar na validação ao cadastrar produto com quantidade negativa")
    void validarProdutoCadastroDTOComQuantidadeNegativa() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Produto", "Descrição", -1, new BigDecimal("10.00"), 1);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<ProdutoCadastroDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("quantidade")));
    }


    @Test
    @DisplayName("Deve lançar PersistenciaEstoqueException ao falhar em gravar lista de produtos")
    void gravarEstoqueComFalha() {
        doThrow(new RuntimeException("Falha")).when(estoqueArquivoRepository).atualizarEstoque(any());

        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Novo", "Descrição", 3, new BigDecimal("10.00"), 3);
        when(estoqueArquivoRepository.buscarTodos()).thenReturn(new ArrayList<>());

        assertThrows(PersistenciaEstoqueException.class, () -> estoqueService.cadastrarOuAtualizarProduto(dto));
    }

    @Test
    @DisplayName("Deve lançar PersistenciaEstoqueException ao falhar em gravar produto")
    void gravarProdutoComFalha() {
        Produto produtoErro = new Produto("Erro", "Falha", 1, 10, new BigDecimal("10.00"));
        produtoErro.setId(99L);

        when(estoqueArquivoRepository.buscarPorId(99L)).thenReturn(Optional.of(produtoErro));
        doThrow(new RuntimeException("Falha")).when(estoqueArquivoRepository).atualizarProduto(any());

        assertThrows(PersistenciaEstoqueException.class, () -> estoqueService.baixarEstoque(99L, 5));
    }
}