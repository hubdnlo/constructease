package br.com.constructease.constructease.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO utilizado para atualizar o nome de um produto no estoque")
public class AtualizarNomeDTO {

    @NotBlank(message = "O nome não pode estar vazio")
    @Schema(description = "Novo nome do produto", example = "Bloco Estrutural 15x40", required = true)
    private String novoNome;
}