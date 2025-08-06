package br.com.constructease.constructease.interfaces;

import br.com.constructease.constructease.dto.PedidoDTO;
import br.com.constructease.constructease.model.Pedido;

import java.util.List;

public interface IPedidoService {
    Pedido criarPedido(PedidoDTO dto);
    double calcularTotalPedido(Pedido pedido);
    void cancelarPedido(Long id);
    List<Pedido> listarTodos();
    Pedido buscarPedidoObrigatorio(Long id);
}
