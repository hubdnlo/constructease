package br.com.constructease.constructease.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemPedidoDTO {

    @NotNull(message = "O ID do produto é obrigatório")
    @Min(value = 1, message = "O ID do produto deve ser maior que zero")
    private final Long produtoId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    private final int quantidade;

    private final Double precoUnitario;
    private final Double subtotal;

    /**
     * Construtor usado para deserialização de requisições JSON.
     */
    @JsonCreator
    public ItemPedidoDTO(
            @JsonProperty("produtoId") Long produtoId,
            @JsonProperty("quantidade") int quantidade
    ) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = null;
        this.subtotal = null;
    }

    /**
     * Construtor usado para montar a resposta com preço e subtotal.
     */
    public ItemPedidoDTO(Long produtoId, int quantidade, Double precoUnitario) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = precoUnitario != null ? precoUnitario * quantidade : null;
    }

    @Override
    public String toString() {
        return "ItemPedidoDTO{" +
                "produtoId=" + produtoId +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", subtotal=" + subtotal +
                '}';
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }
}