package br.com.constructease.constructease.dto;

import br.com.constructease.constructease.model.Produto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoDTO {

    private Integer id;
    private String nome;
    private Integer quantidade;
    private Double preco;
    private String descricao;     // Opcional: se estiver presente no modelo
    private Integer categoriaId;  // Opcional: idem

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId() != null ? produto.getId().intValue() : null;
        this.nome = produto.getNome();
        this.quantidade = produto.getQuantidade();
        this.preco = produto.getPreco();

        //Os campos abaixo não existem no Produto, então mantive como null
        this.descricao = null;
        this.categoriaId = null;
    }

}