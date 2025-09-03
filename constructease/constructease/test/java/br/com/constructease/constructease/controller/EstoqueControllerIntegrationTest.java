package br.com.constructease.constructease.controller;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa o application-test.properties
@DisplayName("Testes de integração para EstoqueController")
class EstoqueControllerIntegrationTest {

    private static final Path TEST_ESTOQUE_PATH = Paths.get("data/test-estoque.json");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void prepararArquivoEstoque() throws IOException {
        Files.createDirectories(TEST_ESTOQUE_PATH.getParent());
        if (!Files.exists(TEST_ESTOQUE_PATH)) {
            Files.writeString(TEST_ESTOQUE_PATH, "[]");
        }
    }

    @Test
    @DisplayName("Deve cadastrar novo produto via POST /estoque/cadastro")
    void cadastrarProduto() throws Exception {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Tijolo", "Cerâmico", 100, new BigDecimal("0.50"), 1);

        mockMvc.perform(post("/estoque/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve listar produtos disponíveis via GET /estoque")
    void listarProdutosDisponiveis() throws Exception {
        mockMvc.perform(get("/estoque"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}