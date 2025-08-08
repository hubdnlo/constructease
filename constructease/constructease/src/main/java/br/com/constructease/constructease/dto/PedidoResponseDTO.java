package br.com.constructease.constructease.dto;

import br.com.constructease.constructease.model.Pedido;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoResponseDTO {

    private final Pedido pedido;
    private final double valorTotal;

    public PedidoResponseDTO(Pedido pedido, double valorTotal) {
        this.pedido = pedido;
        this.valorTotal = valorTotal;
    }

}

