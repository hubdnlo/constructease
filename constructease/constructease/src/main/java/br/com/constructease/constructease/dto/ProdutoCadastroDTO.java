package br.com.constructease.constructease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoCadastroDTO {

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;
    @Positive(message = "A quantidade deve ser maior que zero")
    private int quantidade;
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private double preco;
    @NotNull(message = "A categoria é obrigatória")
    private Integer categoriaId;

    public ProdutoCadastroDTO(String nome, int quantidade, double preco) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }
}