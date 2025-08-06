package br.com.constructease.constructease.controller;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar ou atualizar produto no estoque")
    public ResponseEntity<String> cadastrarProduto(@RequestBody @Valid ProdutoCadastroDTO dto) {
        try {
            estoqueService.cadastrarOuAtualizarProduto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Produto cadastrado ou atualizado com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/nome")
    @Operation(summary = "Atualizar o nome de um produto no estoque")
    public ResponseEntity<String> atualizarNome(@PathVariable int id, @RequestBody String novoNome) {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nome inválido.");
        }
        estoqueService.atualizarNomeProduto(id, novoNome.trim());
        return ResponseEntity.ok("Nome do produto atualizado.");
    }


    @Operation(summary = "Listar itens disponíveis no estoque")
    @GetMapping
    public List<Produto> listarItensDisponiveis() {
        return estoqueService.listarItensDisponiveis();
    }

    @Operation(summary = "Consultar um item no estoque")
    @GetMapping("/{id}")
    public Produto consultarItem(@PathVariable int id) {
        return estoqueService.consultarItem(id);
    }
}
