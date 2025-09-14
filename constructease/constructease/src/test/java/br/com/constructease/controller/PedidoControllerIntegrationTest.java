package br.com.constructease.controller;

import br.com.constructease.dto.ItemPedidoDTO;
import br.com.constructease.dto.PedidoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes de integração para PedidoController")
class PedidoControllerIntegrationTest {

    private static final Path TEST_PEDIDOS_PATH = Paths.get("data/test-pedidos.json");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void prepararArquivoPedidos() throws IOException {
        Files.createDirectories(TEST_PEDIDOS_PATH.getParent());
        if (!Files.exists(TEST_PEDIDOS_PATH)) {
            Files.writeString(TEST_PEDIDOS_PATH, "[]");
        }
    }

    @Test
    @DisplayName("Deve criar novo pedido via POST /api/pedidos")
    void criarPedido() throws Exception {
        ItemPedidoDTO item = new ItemPedidoDTO(1L, 2);
        PedidoDTO dto = new PedidoDTO("Pedido de teste", List.of(item));

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Pedido de teste"));
    }

    @Test
    @DisplayName("Deve listar pedidos via GET /api/pedidos")
    void listarPedidos() throws Exception {
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}