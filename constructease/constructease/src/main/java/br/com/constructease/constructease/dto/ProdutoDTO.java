package br.com.constructease.constructease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO que representa os dados de um produto cadastrado no sistema")
public class ProdutoDTO {

    @Schema(description = "ID do produto", example = "101")
    private Integer id;

    @Schema(description = "Nome do produto", example = "Bloco Estrutural 15x40")
    private String nome;

    @Schema(description = "Quantidade disponível em estoque", example = "200")
    private Integer quantidade;

    @Schema(description = "Preço unitário do produto", example = "29.90")
    private BigDecimal preco;

    @Schema(description = "Descrição detalhada do produto", example = "Bloco de concreto para alvenaria estrutural")
    private String descricao;

    @Schema(description = "ID da categoria do produto", example = "3")
    private Integer categoriaId;
}