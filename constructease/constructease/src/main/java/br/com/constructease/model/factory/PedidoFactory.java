package br.com.constructease.model.factory;

import br.com.constructease.model.Pedido;

import java.util.List;
import java.util.Objects;

/**
 * Fábrica responsável pela criação de objetos Pedido com ID incremental.
 */
public class PedidoFactory {

    /**
     * Cria um novo pedido com ID único, baseado na maior ID presente na lista de pedidos existentes.
     *
     * @param descricao          descrição do novo pedido
     * @param pedidosExistentes  lista atual de pedidos já cadastrados
     * @return novo objeto Pedido com ID incrementado
     * @throws IllegalArgumentException se a lista de pedidos for nula
     */
    public static Pedido criarPedidoIncrementandoId(String descricao, List<Pedido> pedidosExistentes) {
        if (pedidosExistentes == null) {
            throw new IllegalArgumentException("A lista de pedidos existentes não pode ser nula");
        }

        long novoId = pedidosExistentes.stream()
                .map(Pedido::getId)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L) + 1;

        return new Pedido(novoId, descricao);
    }
}