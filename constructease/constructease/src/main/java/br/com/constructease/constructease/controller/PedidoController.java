package br.com.constructease.constructease.controller;

import br.com.constructease.constructease.dto.PedidoDTO;
import br.com.constructease.constructease.dto.PedidoResponseDTO;
import br.com.constructease.constructease.model.Pedido;
import br.com.constructease.constructease.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @PostMapping
    @Operation(summary = "Efetuar um novo pedido")
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = pedidoService.criarPedido(dto);
        double total = pedidoService.calcularTotalPedido(pedido);
        PedidoResponseDTO resposta = new PedidoResponseDTO(pedido, total);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
    @Operation(summary = "Cancelar um pedido")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Lista todos os pedidos ativos")
    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }
}