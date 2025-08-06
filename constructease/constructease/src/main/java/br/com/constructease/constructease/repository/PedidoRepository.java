package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.exception.PedidoNaoEncontradoException;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.model.StatusPedido;
import br.com.constructease.constructease.model.factory.PedidoFactory;
import br.com.constructease.constructease.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public class PedidoRepository {
    private final String caminho = "pedidos.json";

    public List<Pedido> buscarTodos() {
        return JsonUtil.lerJson(caminho, new TypeReference<List<Pedido>>() {});
    }

    public void cancelar(int id) {
        List<Pedido> pedidos = buscarTodos();

        Pedido pedido = pedidos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido com ID " + id + " não encontrado."));

        pedido.setStatus(StatusPedido.CANCELADO);

        JsonUtil.gravarJson(caminho, pedidos);
    }

    public synchronized Pedido save(Pedido pedido) {
        List<Pedido> pedidos = buscarTodos();
        Pedido resultado;

        if (pedido.getId() == null || pedido.getId() <= 0) {
            resultado = PedidoFactory.criarComNovoId(pedido.getDescricao(), pedidos);
            resultado.setItens(pedido.getItens()); // se houver itens
            pedidos.add(resultado);
        } else {
            pedidos.removeIf(p -> p.getId().equals(pedido.getId()));
            pedidos.add(pedido);
            resultado = pedido;
        }

        JsonUtil.gravarJson(caminho, pedidos);
        return resultado;
    }

    public Optional<Pedido> findOptionalById(Long id) {
        List<Pedido> pedidos = buscarTodos();
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

}