package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.model.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedidoRepositoryTest {

    private PedidoRepository pedidoRepository;
    private File pedidosJson;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        pedidosJson = tempDir.resolve("pedidos.json").toFile();
        pedidoRepository = new PedidoRepository(pedidosJson.getAbsolutePath());
    }

    @Test
    void deveSalvarEPesquisarPedidoComReflexao() throws Exception {
        Constructor<Pedido> constructor = Pedido.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Pedido pedido = constructor.newInstance();

        pedido.setId(99L);
        pedido.setDescricao("Pedido de teste");
        pedido.setStatus(StatusPedido.ATIVO);
        pedido.setAtivo(true);

        pedidoRepository.save(pedido);

        Optional<Pedido> encontrado = pedidoRepository.findOptionalById(99L);
        assertTrue(encontrado.isPresent(), "Pedido deveria estar presente");
        assertEquals("Pedido de teste", encontrado.get().getDescricao());
        assertEquals(StatusPedido.ATIVO, encontrado.get().getStatus());
        assertTrue(encontrado.get().isAtivo());
    }
}
