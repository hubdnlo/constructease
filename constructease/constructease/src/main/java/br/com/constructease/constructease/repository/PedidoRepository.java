package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.exception.PedidoNaoEncontradoException;
import br.com.constructease.constructease.exception.PersistenciaPedidoException;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.model.StatusPedido;
import br.com.constructease.constructease.model.factory.PedidoFactory;
import br.com.constructease.constructease.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepository {

    private static final Logger logger = LoggerFactory.getLogger(PedidoRepository.class);

    private final Resource pedidoJson;

    @Autowired
    public PedidoRepository(@Value("${pedido.repository.path}") Resource pedidoJson) {
        this.pedidoJson = pedidoJson;
    }

    public List<Pedido> buscarTodos() {
        try (InputStream input = pedidoJson.getInputStream()) {
            return JsonUtil.lerJson(input, new TypeReference<List<Pedido>>() {});
        } catch (IOException e) {
            logger.warn("Erro ao ler arquivo de pedidos: {}", pedidoJson.getFilename(), e);
            return List.of();
        }
    }

    public void cancelar(int id) {
        List<Pedido> pedidos = buscarTodos();
        Pedido pedido = pedidos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido com ID " + id + " não encontrado."));

        pedido.setStatus(StatusPedido.CANCELADO);
        gravarPedidos(pedidos);
        logger.info("Pedido com ID {} foi cancelado.", id);
    }

    public synchronized Pedido save(Pedido pedido) {
        if (pedido.getId() == null || pedido.getId() <= 0) {
            return criarPedido(pedido);
        } else {
            return atualizarPedido(pedido);
        }
    }

    private Pedido criarPedido(Pedido pedido) {
        List<Pedido> pedidos = buscarTodos();
        Pedido novoPedido = PedidoFactory.criarPedidoIncrementandoId(pedido.getDescricao(), pedidos);
        novoPedido.setItens(pedido.getItens());
        pedidos.add(novoPedido);
        gravarPedidos(pedidos);
        logger.info("Novo pedido criado com ID {}.", novoPedido.getId());
        return novoPedido;
    }

    private Pedido atualizarPedido(Pedido pedido) {
        List<Pedido> pedidos = buscarTodos();
        Optional<Pedido> existente = pedidos.stream()
                .filter(p -> p.getId().equals(pedido.getId()))
                .findFirst();

        if (existente.isEmpty()) {
            throw new PedidoNaoEncontradoException("Pedido com ID " + pedido.getId() + " não encontrado para atualização.");
        }

        pedidos.remove(existente.get());
        pedidos.add(pedido);
        gravarPedidos(pedidos);
        logger.info("Pedido com ID {} foi atualizado.", pedido.getId());
        return pedido;
    }

    private void gravarPedidos(List<Pedido> pedidos) {
        try {
            String caminhoFisico = pedidoJson.getFile().getPath();
            JsonUtil.gravarJson(caminhoFisico, pedidos);
        } catch (IOException e) {
            logger.error("Erro ao gravar pedidos no arquivo: {}", pedidoJson.getFilename(), e);
            throw new PersistenciaPedidoException("Falha ao gravar pedidos no arquivo.", e);
        }
    }

    public Optional<Pedido> findOptionalById(Long id) {
        return buscarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
}