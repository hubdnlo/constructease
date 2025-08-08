package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ItemPedidoDTO;
import br.com.constructease.constructease.dto.PedidoDTO;
import br.com.constructease.constructease.interfaces.IPedidoService;
import br.com.constructease.constructease.model.StatusPedido;
import br.com.constructease.constructease.exception.PedidoNaoEncontradoException;
import br.com.constructease.constructease.model.ItemPedido;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService implements IPedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EstoqueService estoqueService;

    public PedidoService(PedidoRepository pedidoRepository, EstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueService = estoqueService;
    }


    public Pedido criarPedido(PedidoDTO dto) {
        validarPedidoDTO(dto);

        Pedido pedido = new Pedido(dto.getDescricao());
        pedido.setStatus(StatusPedido.ATIVO);

        for (ItemPedidoDTO itemDTO : dto.getItens()) {
            double precoUnitario = estoqueService.getPrecoProduto(itemDTO.getProdutoId());
            estoqueService.baixarEstoque(itemDTO.getProdutoId(), itemDTO.getQuantidade());

            ItemPedido item = new ItemPedido(itemDTO.getProdutoId(), itemDTO.getQuantidade(), precoUnitario);
            pedido.adicionarItem(item);
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        double valorTotal = calcularTotalPedido(pedidoSalvo);

        System.out.printf("Pedido criado | ID: %d | Valor Total: R$ %.2f%n", pedidoSalvo.getId(), valorTotal);
        return pedidoSalvo;
    }

    private void validarPedidoDTO(PedidoDTO dto) {
        if (dto == null || dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido inválido: deve conter itens.");
        }
    }

    public double calcularTotalPedido(Pedido pedido) {
        return pedido.getItens() == null ? 0.0 :
                pedido.getItens().stream()
                        .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
                        .sum();
    }

    public void cancelarPedido(Long id) {
        Pedido pedido = buscarPedidoObrigatorio(id);

        if (!pedido.getStatus().equals(StatusPedido.ATIVO)) {
            throw new IllegalStateException("Pedido não pode ser cancelado. Status atual: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.buscarTodos();
    }

    public Pedido buscarPedidoObrigatorio(Long id) {
        return pedidoRepository.findOptionalById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado: id=" + id));
    }

}