package br.com.constructease.constructease.dto;

import br.com.constructease.constructease.model.ItemPedido;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.service.EstoqueService;
import br.com.constructease.constructease.util.FormatadorDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "descricao", "status", "itens", "valorTotal" })
public class PedidoResponseDTO {

    private final Long id;
    private final String descricao;
    private final String status;
    private final double valorTotal;
    private final List<ItemPedidoDTO> itens;

    public PedidoResponseDTO(Pedido pedido, double valorTotal, EstoqueService estoqueService) {
        this.id = pedido.getId();
        this.descricao = pedido.getDescricao();
        this.status = pedido.getStatus().name();
        this.valorTotal = FormatadorDecimal.arredondar(valorTotal);
        this.itens = mapearItens(pedido.getItens(), estoqueService);
    }

    private List<ItemPedidoDTO> mapearItens(List<ItemPedido> itensPedido, EstoqueService estoqueService) {
        if (itensPedido == null || itensPedido.isEmpty()) return List.of();

        return itensPedido.stream()
                .map(item -> new ItemPedidoDTO(
                        item.getProdutoId(),
                        estoqueService.getNomeProduto(item.getProdutoId()),
                        item.getQuantidade(),
                        item.getPrecoUnitario()))
                .collect(Collectors.toList());
    }
}