package br.com.constructease.constructease.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AtualizarNomeDTO {
    @NotBlank(message = "O nome não pode estar vazio")
    private String novoNome;

//    public String getNovoNome() {
//        return novoNome;
//    }
//
//    public void setNovoNome(String novoNome) {
//        this.novoNome = novoNome;
//    }
}