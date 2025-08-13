package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ItemPedidoDTO;
import br.com.constructease.constructease.dto.PedidoDTO;
import br.com.constructease.constructease.interfaces.IPedidoService;
import br.com.constructease.constructease.model.StatusPedido;
import br.com.constructease.constructease.exception.PedidoNaoEncontradoException;
import br.com.constructease.constructease.model.ItemPedido;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class PedidoService implements IPedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EstoqueService estoqueService;

    public PedidoService(PedidoRepository pedidoRepository, EstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueService = estoqueService;
    }

    @Transactional
    public Pedido criarPedido(PedidoDTO dto) {
        logger.debug("Iniciando criação de pedido: {}", dto);
        validarPedidoDTO(dto);

        Pedido pedido = new Pedido(dto.getDescricao());
        pedido.setStatus(StatusPedido.ATIVO);

        for (ItemPedidoDTO itemDTO : dto.getItens()) {
            double precoUnitario = estoqueService.getPrecoProduto(itemDTO.getProdutoId());
            estoqueService.baixarEstoque(itemDTO.getProdutoId(), itemDTO.getQuantidade());

            ItemPedido item = new ItemPedido(itemDTO.getProdutoId(), itemDTO.getQuantidade(), precoUnitario);
            pedido.adicionarItem(item);

            logger.debug("Item adicionado ao pedido | Produto ID: {} | Quantidade: {} | Preço Unitário: R$ {}",
                    itemDTO.getProdutoId(), itemDTO.getQuantidade(), precoUnitario);
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        double valorTotal = calcularTotalPedido(pedidoSalvo);

        logger.info("Pedido criado com sucesso | ID: {} | Valor Total: R$ {}", pedidoSalvo.getId(), valorTotal);
        return pedidoSalvo;
    }

    @Transactional
    public void cancelarPedido(Long id) {
        logger.warn("Tentando cancelar pedido | ID: {}", id);

        Pedido pedido = buscarPedidoObrigatorio(id);

        if (!pedido.getStatus().equals(StatusPedido.ATIVO)) {
            logger.error("Cancelamento inválido | ID: {} | Status atual: {}", id, pedido.getStatus());
            throw new IllegalStateException("Pedido não pode ser cancelado. Status atual: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);

        logger.info("Pedido cancelado com sucesso | ID: {}", id);
    }

    public double calcularTotalPedido(Pedido pedido) {
        double total = pedido.getItens() == null ? 0.0 :
                pedido.getItens().stream()
                        .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
                        .sum();

        logger.debug("Total calculado para pedido ID {}: R$ {}", pedido.getId(), total);
        return total;
    }

    public List<Pedido> listarTodos() {
        logger.debug("Listando todos os pedidos");
        return pedidoRepository.buscarTodos();
    }

    public Pedido buscarPedidoObrigatorio(Long id) {
        logger.debug("Buscando pedido obrigatório | ID: {}", id);
        return pedidoRepository.findOptionalById(id)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado | ID: {}", id);
                    return new PedidoNaoEncontradoException("Pedido não encontrado: id=" + id);
                });
    }

    private void validarPedidoDTO(PedidoDTO dto) {
        logger.debug("Validando PedidoDTO");
        if (dto == null || dto.getItens() == null || dto.getItens().isEmpty()) {
            logger.error("Pedido inválido: DTO nulo ou sem itens");
            throw new IllegalArgumentException("Pedido inválido: deve conter itens.");
        }
    }
}