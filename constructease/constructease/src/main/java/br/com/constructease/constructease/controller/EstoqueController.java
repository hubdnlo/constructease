package br.com.constructease.constructease.controller;

import br.com.constructease.constructease.dto.AtualizarNomeDTO;
import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.dto.ProdutoDTO;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar ou atualizar produto no estoque")
    public ResponseEntity<String> cadastrarProduto(@RequestBody @Valid ProdutoCadastroDTO dto) {
        estoqueService.cadastrarOuAtualizarProduto(dto);
        return ResponseEntity.status(201).body("Produto cadastrado ou atualizado com sucesso.");
    }

    @PutMapping("/{id}/nome")
    @Operation(summary = "Atualizar o nome de um produto no estoque")
    public ResponseEntity<String> atualizarNome(@PathVariable Long id, @RequestBody @Valid AtualizarNomeDTO dto) {
        estoqueService.atualizarNomeProduto(id, dto.getNovoNome().trim());
        return ResponseEntity.ok("Nome do produto atualizado.");
    }

    @GetMapping
    @Operation(summary = "Listar itens disponíveis no estoque")
    public ResponseEntity<List<ProdutoDTO>> listarItensDisponiveis() {
        List<Produto> produtos = estoqueService.listarItensDisponiveis();
        List<ProdutoDTO> dtos = produtos.stream().map(ProdutoDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar um item no estoque")
    public ResponseEntity<ProdutoDTO> consultarItem(@PathVariable Long id) {
        Produto produto = estoqueService.consultarItem(id);
        return ResponseEntity.ok(new ProdutoDTO(produto));
    }
}