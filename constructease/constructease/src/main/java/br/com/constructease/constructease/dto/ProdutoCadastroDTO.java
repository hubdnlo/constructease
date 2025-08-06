package br.com.constructease.constructease.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoCadastroDTO {

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @Positive(message = "A quantidade deve ser maior que zero")
    private int quantidade;

    @Positive(message = "O preço deve ser maior que zero")
    private double preco;

    public void setDescricao(String s) {
    }

    public void setCategoriaId(Integer i) {
    }
}

