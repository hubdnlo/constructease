package br.com.constructease.constructease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO utilizado para cadastrar um novo produto no sistema")
public class ProdutoCadastroDTO {

    @Schema(description = "Nome do produto", example = "Bloco Estrutural 15x40", required = true)
    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Bloco de concreto para alvenaria estrutural", required = true)
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @Schema(description = "Quantidade disponível em estoque", example = "100", required = true)
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private int quantidade;

    @Schema(description = "Preço unitário do produto", example = "29.90", required = true)
    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    private BigDecimal preco;

    @Schema(description = "ID da categoria à qual o produto pertence", example = "3", required = true)
    @NotNull(message = "O id da categoria é obrigatório")
    private Integer categoriaId;
}