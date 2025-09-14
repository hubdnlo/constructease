package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.model.ItemPedido;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.model.StatusPedido;
import br.com.constructease.constructease.util.JsonUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test") // Usa o application-test.properties
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes de integração para PedidoRepository")
class PedidoRepositoryIntegrationTest {

    private static final Path TEST_PEDIDOS_PATH = Paths.get("data/test-pedidos.json");

    @Autowired
    private PedidoRepository repository;

    @BeforeEach
    void prepararArquivoPedidos() throws IOException {
        Files.createDirectories(TEST_PEDIDOS_PATH.getParent());
        Files.writeString(TEST_PEDIDOS_PATH, "[]"); // Limpa o arquivo antes de cada teste
    }

    @Test
    @Order(1)
    @DisplayName("Deve gravar pedido diretamente no arquivo JSON e verificar leitura")
    void salvarPedido() {
        ItemPedido item = new ItemPedido(1L, 3, new BigDecimal("25.00"));
        Pedido pedido = new Pedido(1L, "Pedido de cimento", List.of(item));
        pedido.setId(1L); // ID manual para controle nos testes

        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(pedido);

        JsonUtil.gravarJson(TEST_PEDIDOS_PATH.toString(), pedidos); // Usa caminho de teste

        List<Pedido> resultado = repository.buscarTodos();
        assertTrue(resultado.stream().anyMatch(p -> p.getDescricao().equals("Pedido de cimento")));
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar todos os pedidos salvos no arquivo JSON")
    void buscarTodosPedidos() {
        ItemPedido item = new ItemPedido(2L, 1, new BigDecimal("10.00"));
        Pedido pedido = new Pedido(1L,"Pedido de areia", List.of(item));
        pedido.setId(2L);
        pedido.setStatus(StatusPedido.ATIVO);
        pedido.setValorTotal(new BigDecimal("10.00"));

        JsonUtil.gravarJson(TEST_PEDIDOS_PATH.toString(), List.of(pedido));

        List<Pedido> pedidos = repository.buscarTodos();
        assertFalse(pedidos.isEmpty());
        assertTrue(pedidos.stream().anyMatch(p -> p.getDescricao().equals("Pedido de areia")));
    }

}