package br.com.constructease.constructease.dto;

import br.com.constructease.constructease.model.Pedido;
import lombok.Getter;

@Getter
public class PedidoResponseDTO {

    private final Pedido pedido;
    private final double valorTotal;

    public PedidoResponseDTO(Pedido pedido, double valorTotal) {
        this.pedido = pedido;
        this.valorTotal = valorTotal;
    }

}

