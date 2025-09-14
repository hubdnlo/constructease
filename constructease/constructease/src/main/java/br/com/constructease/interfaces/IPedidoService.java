package br.com.constructease.interfaces;

import br.com.constructease.dto.PedidoDTO;
import br.com.constructease.dto.PedidoResponseDTO;
import br.com.constructease.model.Pedido;
import br.com.constructease.model.StatusPedido;

import java.math.BigDecimal;
import java.util.List;

public interface IPedidoService {

    Pedido criarPedido(PedidoDTO dto);

    BigDecimal calcularTotalPedido(Pedido pedido);

    void cancelarPedido(Long id);

    List<Pedido> listarTodos();

    List<Pedido> listarPedidosAtivos();

    List<Pedido> listarPorStatus(StatusPedido status);

    Pedido buscarPedidoObrigatorio(Long id);

    PedidoResponseDTO gerarPedidoResponseDTO(Pedido pedido);
}