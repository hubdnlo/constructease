package br.com.constructease.constructease.dto;

import br.com.constructease.constructease.model.Produto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import br.com.constructease.constructease.util.FormatadorDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private int quantidade;
    private double preco;
    private Integer categoriaId;

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.quantidade = produto.getQuantidade();
        this.preco = FormatadorDecimal.arredondar(produto.getPreco());
        this.categoriaId = produto.getCategoriaId();
    }
}