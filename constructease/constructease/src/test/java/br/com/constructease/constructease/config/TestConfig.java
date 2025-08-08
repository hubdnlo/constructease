package br.com.constructease.constructease.config;

import br.com.constructease.constructease.repository.EstoqueRepository;
import br.com.constructease.constructease.repository.PedidoRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public EstoqueRepository estoqueRepository() {
        return new EstoqueRepository("src/test/java/repository/estoque.json");
    }

    @Bean
    public PedidoRepository pedidoRepository() {
        return new PedidoRepository("src/test/java/repository/pedidos.json");
    }
}


