package br.com.constructease.constructease.dto;

import br.com.constructease.constructease.model.ItemPedido;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.service.EstoqueService;
import br.com.constructease.constructease.util.FormatadorDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "descricao", "status", "itens", "valorTotal" })
@Schema(description = "DTO de resposta que representa os dados completos de um pedido realizado")
public class PedidoResponseDTO {

    @Schema(description = "Identificador único do pedido", example = "1001")
    private final Long id;

    @Schema(description = "Descrição do pedido", example = "Pedido de materiais para obra da Rua A")
    private final String descricao;

    @Schema(description = "Status atual do pedido", example = "ATIVO")
    private final String status;

    @Schema(description = "Valor total do pedido", example = "459.90")
    private final BigDecimal valorTotal;

    @Schema(description = "Lista de itens que compõem o pedido")
    private final List<ItemPedidoDTO> itens;

    public PedidoResponseDTO(Long id, String descricao, String status, BigDecimal valorTotal, List<ItemPedidoDTO> itens) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.valorTotal = valorTotal;
        this.itens = itens;
    }
}