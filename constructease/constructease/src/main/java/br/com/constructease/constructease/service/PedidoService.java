package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ItemPedidoDTO;
import br.com.constructease.constructease.dto.PedidoDTO;
import br.com.constructease.constructease.dto.PedidoResponseDTO;
import br.com.constructease.constructease.exception.PedidoNaoEncontradoException;
import br.com.constructease.constructease.interfaces.IPedidoService;
import br.com.constructease.constructease.model.ItemPedido;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.model.StatusPedido;
import br.com.constructease.constructease.repository.PedidoRepository;
import br.com.constructease.constructease.util.FormatadorDecimal;
import br.com.constructease.constructease.util.JsonUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService implements IPedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    private static final String CAMINHO_JSON = "data/pedidos.json";

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

        validarEstoqueDosItens(dto.getItens());

        Pedido pedido = new Pedido(dto.getDescricao());
        pedido.setStatus(StatusPedido.ATIVO);

        for (ItemPedidoDTO itemDTO : dto.getItens()) {
            double precoUnitario = estoqueService.getPrecoProduto(itemDTO.getProdutoId());
            ItemPedido item = new ItemPedido(itemDTO.getProdutoId(), itemDTO.getQuantidade(), precoUnitario);
            pedido.adicionarItem(item);

            logger.debug("Item adicionado ao pedido | Produto ID: {} | Quantidade: {} | Preço Unitário: R$ {}",
                    itemDTO.getProdutoId(), itemDTO.getQuantidade(), precoUnitario);
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        double valorTotal = calcularTotalPedido(pedidoSalvo);
        pedidoSalvo.setValorTotal(valorTotal);

        for (ItemPedido item : pedidoSalvo.getItens()) {
            estoqueService.baixarEstoque(item.getProdutoId(), item.getQuantidade());
        }

        logger.info("Pedido criado com sucesso | ID: {} | Valor Total: R$ {}", pedidoSalvo.getId(), valorTotal);

        try {
            List<Pedido> pedidosExistentes = new ArrayList<>(JsonUtil.lerLista(CAMINHO_JSON, Pedido[].class));
            logger.debug("Total de pedidos existentes antes da gravação: {}", pedidosExistentes.size());

            boolean jaExiste = pedidosExistentes.stream()
                    .anyMatch(p -> p.getId().equals(pedidoSalvo.getId()));

            if (!jaExiste) {
                pedidosExistentes.add(pedidoSalvo);
                logger.debug("Pedido ID {} adicionado à lista para gravação", pedidoSalvo.getId());
            } else {
                logger.warn("Pedido ID {} já existe na lista. Não será adicionado novamente.", pedidoSalvo.getId());
            }

            String caminhoTemp = "data/pedidos_temp.json";
            JsonUtil.gravarJson(caminhoTemp, pedidosExistentes);

            List<Pedido> validacao = JsonUtil.lerLista(caminhoTemp, Pedido[].class);
            if (!validacao.isEmpty()) {
                Files.move(Paths.get(caminhoTemp), Paths.get(CAMINHO_JSON), StandardCopyOption.REPLACE_EXISTING);
                logger.info("Pedido gravado com sucesso no arquivo JSON");
            } else {
                logger.error("Arquivo temporário está vazio após gravação. Não será movido.");
            }
        } catch (Exception e) {
            logger.warn("Pedido foi criado e salvo no banco, mas ocorreu um erro ao gravar no JSON", e);
        }

        return pedidoSalvo;
    }

    private void validarEstoqueDosItens(List<ItemPedidoDTO> itens) {
        for (ItemPedidoDTO item : itens) {
            if (!estoqueService.isDisponivel(item.getProdutoId(), item.getQuantidade())) {
                logger.error("Estoque insuficiente | Produto ID: {} | Quantidade solicitada: {}", item.getProdutoId(), item.getQuantidade());
                throw new IllegalArgumentException("Produto ID " + item.getProdutoId() + " sem estoque suficiente.");
            }
        }
    }

    @Transactional
    public void cancelarPedido(Long id) {
        logger.warn("Tentando cancelar pedido | ID: {}", id);

        Pedido pedido = buscarPedidoObrigatorio(id);

        if (!pedido.getStatus().equals(StatusPedido.ATIVO)) {
            logger.error("Cancelamento inválido | ID: {} | Status atual: {}", id, pedido.getStatus());
            throw new IllegalStateException("Pedido não pode ser cancelado. Status atual: " + pedido.getStatus());
        }

        for (ItemPedido item : pedido.getItens()) {
            estoqueService.devolverEstoque(item.getProdutoId(), item.getQuantidade());
            logger.info("Produto devolvido ao estoque | Produto ID: {} | Quantidade: {}", item.getProdutoId(), item.getQuantidade());
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

        double totalArredondado = FormatadorDecimal.arredondar(total);
        logger.debug("Total calculado para pedido ID {}: R$ {}", pedido.getId(), totalArredondado);
        return totalArredondado;
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

    public PedidoResponseDTO gerarPedidoResponseDTO(Pedido pedido) {
        double valorTotal = calcularTotalPedido(pedido);
        return new PedidoResponseDTO(pedido, valorTotal, estoqueService);
    }
}