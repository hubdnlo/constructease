package br.com.constructease.constructease.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import br.com.constructease.constructease.util.FormatadorDecimal;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemPedidoDTO {

    @NotNull(message = "O ID do produto é obrigatório")
    @Min(value = 1, message = "O ID do produto deve ser maior que zero")
    private final Long produtoId;

    private final String nomeProduto;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    private final int quantidade;

    private final Double precoUnitario;
    private final Double subtotal;

    @JsonCreator
    public ItemPedidoDTO(
            @JsonProperty("produtoId") Long produtoId,
            @JsonProperty("quantidade") int quantidade
    ) {
        this.produtoId = produtoId;
        this.nomeProduto = null;
        this.quantidade = quantidade;
        this.precoUnitario = null;
        this.subtotal = null;
    }

    public ItemPedidoDTO(Long produtoId, String nomeProduto, int quantidade, Double precoUnitario) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario != null ? FormatadorDecimal.arredondar(precoUnitario) : null;
        this.subtotal = (precoUnitario != null) ? FormatadorDecimal.arredondar(precoUnitario * quantidade) : null;
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