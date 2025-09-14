package br.com.constructease.dto;

import br.com.constructease.model.Produto;
import br.com.constructease.util.FormatadorDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO de resposta que representa os dados de um produto cadastrado no sistema")
public class ProdutoResponseDTO {

    @Schema(description = "ID do produto", example = "101")
    private Long id;

    @Schema(description = "Nome do produto", example = "Bloco Estrutural 15x40")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Bloco de concreto para alvenaria estrutural")
    private String descricao;

    @Schema(description = "Quantidade disponível em estoque", example = "200")
    private int quantidade;

    @Schema(description = "Preço unitário do produto", example = "29.90")
    private BigDecimal preco;

    @Schema(description = "ID da categoria do produto", example = "3")
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