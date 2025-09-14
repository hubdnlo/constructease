package br.com.constructease.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import br.com.constructease.util.FormatadorDecimal;

import java.math.BigDecimal;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO que representa um item dentro de um pedido, incluindo produto, quantidade e valores")
public class ItemPedidoDTO {

    @Schema(description = "ID do produto", example = "42", required = true)
    @NotNull(message = "O ID do produto é obrigatório")
    @Min(value = 1, message = "O ID do produto deve ser maior que zero")
    private final Long produtoId;

    @Schema(description = "Nome do produto", example = "Cimento CP II-E 25kg")
    private final String nomeProduto;

    @Schema(description = "Quantidade solicitada do produto", example = "10", required = true)
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    private final Integer quantidade;

    @Schema(description = "Preço unitário do produto", example = "29.90")
    private final BigDecimal precoUnitario;

    @Schema(description = "Subtotal calculado (preço unitário x quantidade)", example = "299.00")
    private final BigDecimal subtotal;

    @JsonCreator
    public ItemPedidoDTO(
            @JsonProperty("produtoId") Long produtoId,
            @JsonProperty("quantidade") Integer quantidade
    ) {
        this.produtoId = produtoId;
        this.nomeProduto = null;
        this.quantidade = quantidade;
        this.precoUnitario = null;
        this.subtotal = null;
    }

    public ItemPedidoDTO(Long produtoId, String nomeProduto, Integer quantidade, BigDecimal precoUnitario) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;

        if (precoUnitario != null) {
            this.precoUnitario = FormatadorDecimal.arredondar(precoUnitario);
            this.subtotal = FormatadorDecimal.arredondar(precoUnitario.multiply(BigDecimal.valueOf(quantidade)));
        } else {
            this.precoUnitario = null;
            this.subtotal = null;
        }
    }

    @Override
    public String toString() {
        return "ItemPedidoDTO{" +
                "produtoId=" + produtoId +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}