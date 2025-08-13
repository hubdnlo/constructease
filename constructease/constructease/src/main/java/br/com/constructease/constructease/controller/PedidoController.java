package br.com.constructease.constructease.controller;

import br.com.constructease.constructease.dto.PedidoDTO;
import br.com.constructease.constructease.dto.PedidoResponseDTO;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.service.PedidoService;
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
        double total = pedidoService.calcularTotalPedido(pedido);
        PedidoResponseDTO resposta = new PedidoResponseDTO(pedido, total);

        logger.info("Pedido criado com sucesso | ID: {} | Total: R$ {}", pedido.getId(), total);
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
    @Operation(summary = "Lista todos os pedidos ativos")
    public ResponseEntity<List<Pedido>> listar() {
        logger.debug("Recebida requisição para listar todos os pedidos");
        List<Pedido> pedidos = pedidoService.listarTodos();
        logger.info("Total de pedidos retornados: {}", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }
}