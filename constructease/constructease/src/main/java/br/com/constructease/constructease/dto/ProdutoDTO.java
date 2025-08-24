package br.com.constructease.constructease.dto;

import br.com.constructease.constructease.model.Produto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import br.com.constructease.constructease.util.FormatadorDecimal;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoDTO {

    private Integer id;
    private String nome;
    private Integer quantidade;
    private BigDecimal preco;
    private String descricao;
    private Integer categoriaId;

//    public ProdutoDTO(Produto produto) {
//        this.id = produto.getId() != null ? produto.getId().intValue() : null;
//        this.nome = produto.getNome();
//        this.quantidade = produto.getQuantidade();
//        this.preco = FormatadorDecimal.arredondar(produto.getPreco());
//        this.descricao = null;
//        this.categoriaId = null;
//    }
}