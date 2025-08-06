package br.com.constructease.constructease.model.factory;

import br.com.constructease.constructease.model.Pedido;

import java.util.List;

public class PedidoFactory {
    public static Pedido criarComNovoId(String descricao, List<Pedido> pedidosExistentes) {
        long novoId = pedidosExistentes.stream()
                .mapToLong(Pedido::getId)
                .max()
                .orElse(0L) + 1;
        return new Pedido(novoId, descricao);
    }
}
