package br.com.constructease.constructease.model.factory;

import br.com.constructease.constructease.model.Pedido;

import java.util.List;

public class PedidoFactory {
    public static Pedido criarPedidoIncrementandoId(String descricao, List<Pedido> pedidosExistentes) {
        if (pedidosExistentes == null) {
            throw new IllegalArgumentException("A lista de pedidos existentes não pode ser nula");
        }

        long novoId = pedidosExistentes.stream()
                .mapToLong(Pedido::getId)
                .max()
                .orElse(0L) + 1;

        return new Pedido(novoId, descricao);
    }

}
