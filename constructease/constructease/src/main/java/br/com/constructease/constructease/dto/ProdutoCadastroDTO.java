package br.com.constructease.constructease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.constructease.constructease.util.FormatadorDecimal;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoCadastroDTO {

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private int quantidade;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    private BigDecimal preco;

    @NotNull(message = "O id da categoria é obrigatório")
    private Integer categoriaId;

//    public ProdutoCadastroDTO(String nome, String descricao, int quantidade, BigDecimal preco, Integer categoriaId) {
//        this.nome = nome;
//        this.descricao = descricao;
//        this.quantidade = quantidade;
//        this.preco = FormatadorDecimal.arredondar(preco);
//        this.categoriaId = categoriaId;
//    }

}