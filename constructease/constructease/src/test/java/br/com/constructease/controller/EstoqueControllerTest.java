package br.com.constructease.controller;

import br.com.constructease.dto.AtualizarNomeDTO;
import br.com.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.dto.ProdutoResponseDTO;
import br.com.constructease.model.Produto;
import br.com.constructease.service.EstoqueService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes unitários para EstoqueController")
class EstoqueControllerTest {

    @InjectMocks
    private EstoqueController controller;

    @Mock
    private EstoqueService estoqueService;

    private Produto produto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        produto = new Produto("Cimento", "CP-II", 1, 10, new BigDecimal("25.00"));
        produto.setId(1L);
    }

    @Test
    @DisplayName("Deve cadastrar ou atualizar produto e retornar status 201")
    void cadastrarProdutoComSucesso() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Cimento", "CP-II", 1, new BigDecimal("25.00"), 10);

        doNothing().when(estoqueService).cadastrarOuAtualizarProduto(dto);

        ResponseEntity<String> response = controller.cadastrarProduto(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Produto cadastrado ou atualizado com sucesso.", response.getBody());
        verify(estoqueService).cadastrarOuAtualizarProduto(dto);
    }

    @Test
    @DisplayName("Deve atualizar nome do produto e retornar status 200")
    void atualizarNomeComSucesso() {
        AtualizarNomeDTO dto = new AtualizarNomeDTO();
        dto.setNovoNome("Cimento Premium");

        doNothing().when(estoqueService).atualizarNomeProduto(eq(1L), anyString());

        ResponseEntity<String> response = controller.atualizarNome(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Nome do produto atualizado.", response.getBody());

        verify(estoqueService).atualizarNomeProduto(1L, "Cimento Premium");
    }

    @Test
    @DisplayName("Deve listar itens disponíveis e retornar lista de ProdutoResponseDTO")
    void listarItensDisponiveisComSucesso() {
        when(estoqueService.listarItensDisponiveis()).thenReturn(List.of(produto));

        ResponseEntity<List<ProdutoResponseDTO>> response = controller.listarItensDisponiveis();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Cimento", response.getBody().get(0).getNome());
        verify(estoqueService).listarItensDisponiveis();
    }

    @Test
    @DisplayName("Deve consultar item por ID e retornar ProdutoResponseDTO")
    void consultarItemComSucesso() {
        when(estoqueService.consultarItem(1L)).thenReturn(produto);

        ResponseEntity<ProdutoResponseDTO> response = controller.consultarItem(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cimento", response.getBody().getNome());
        verify(estoqueService).consultarItem(1L);
    }
}