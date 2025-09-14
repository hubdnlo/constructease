package br.com.constructease.controller;

import br.com.constructease.dto.PedidoDTO;
import br.com.constructease.dto.PedidoResponseDTO;
import br.com.constructease.model.Pedido;
import br.com.constructease.model.StatusPedido;
import br.com.constructease.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Efetuar um novo pedido")
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoDTO dto) {
        logger.info("Recebida requisição para criar pedido: {}", dto);

        Pedido pedido = pedidoService.criarPedido(dto);
        PedidoResponseDTO resposta = pedidoService.gerarPedidoResponseDTO(pedido);

        logger.info("Pedido criado com sucesso | ID: {} | Total: R$ {}", pedido.getId(), resposta.getValorTotal());
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar um pedido")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        logger.warn("Recebida requisição para cancelar pedido | ID: {}", id);

        pedidoService.cancelarPedido(id);

        logger.info("Pedido cancelado com sucesso | ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Lista pedidos, podendo filtrar por status")
    public ResponseEntity<List<PedidoResponseDTO>> listar(@RequestParam(required = false) StatusPedido status) {
        logger.debug("Recebida requisição para listar pedidos com status: {}", status);

        List<Pedido> pedidos = (status != null)
                ? pedidoService.listarPorStatus(status)
                : pedidoService.listarTodos();

        List<PedidoResponseDTO> resposta = pedidos.stream()
                .map(pedidoService::gerarPedidoResponseDTO)
                .toList();

        logger.info("Total de pedidos retornados: {}", resposta.size());
        return ResponseEntity.ok(resposta);
    }
}