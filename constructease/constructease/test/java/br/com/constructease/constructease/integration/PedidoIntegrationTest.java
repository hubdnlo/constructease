//package br.com.constructease.constructease.integration;
//
//import br.com.constructease.constructease.config.TestConfig;
//import br.com.constructease.constructease.dto.ItemPedidoDTO;
//import br.com.constructease.constructease.dto.PedidoDTO;
//import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
//import br.com.constructease.constructease.model.Pedido;
//import br.com.constructease.constructease.repository.PedidoRepository;
//import br.com.constructease.constructease.repository.EstoqueRepository;
//import br.com.constructease.constructease.service.EstoqueService;
//import br.com.constructease.constructease.service.PedidoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(classes = TestConfig.class)
//@ActiveProfiles("test")
//class PedidoIntegrationTest {
//
//    @Autowired
//    private EstoqueRepository estoqueRepository;
//
//    @Autowired
//    private PedidoRepository pedidoRepository;
//
//    private PedidoService pedidoService;
//    private EstoqueService estoqueService;
//
//    @Value("${pedido.repository.path}")
//    private String caminhoPedidos;
//
//    @Value("${estoque.repository.path}")
//    private String caminhoEstoque;
//
//    @BeforeEach
//    void setup() throws Exception {
//        Files.createDirectories(Path.of("src/test/resources/repository"));
//        Files.deleteIfExists(Path.of(caminhoPedidos));
//        Files.deleteIfExists(Path.of(caminhoEstoque));
//
//        estoqueService = new EstoqueService(estoqueRepository);
//        pedidoService = new PedidoService(pedidoRepository, estoqueService);
//
//        ProdutoCadastroDTO produtoDTO = new ProdutoCadastroDTO();
//        produtoDTO.setNome("Produto Teste");
//        produtoDTO.setQuantidade(10);
//        produtoDTO.setPreco(100.0);
//        produtoDTO.setDescricao("Produto para integração");
//        produtoDTO.setCategoriaId(1);
//
//        estoqueService.cadastrarOuAtualizarProduto(produtoDTO);
//    }
//
//    @Test
//    void fluxoCompleto_deveCriarCancelarEPersistirPedido() {
//        PedidoDTO dto = new PedidoDTO("Pedido Integração", List.of(new ItemPedidoDTO(1, 1)));
//        Pedido pedido = pedidoService.criarPedido(dto);
//
//        assertNotNull(pedido.getId());
//        assertEquals("ATIVO", pedido.getStatus().name());
//
//        pedidoService.cancelarPedido(pedido.getId());
//
//        Pedido pedidoCancelado = pedidoService.buscarPedidoObrigatorio(pedido.getId());
//        assertEquals("CANCELADO", pedidoCancelado.getStatus().name());
//    }
//}
