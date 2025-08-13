package br.com.constructease.constructease.dto;

import br.com.constructease.constructease.model.ItemPedido;
import br.com.constructease.constructease.model.Pedido;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoResponseDTO {

    private final Long id;
    private final String descricao;
    private final String status;
    private final double valorTotal;
    private final List<ItemPedidoDTO> itens;

    public PedidoResponseDTO(Pedido pedido, double valorTotal) {
        this.id = pedido.getId();
        this.descricao = pedido.getDescricao();
        this.status = pedido.getStatus().name();
        this.valorTotal = valorTotal;
        this.itens = mapearItens(pedido.getItens());
    }

    private List<ItemPedidoDTO> mapearItens(List<ItemPedido> itensPedido) {
        if (itensPedido == null || itensPedido.isEmpty()) return List.of();

        return itensPedido.stream()
                .map(item -> new ItemPedidoDTO(
                        item.getProdutoId(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()))
                .collect(Collectors.toList());
    }
}