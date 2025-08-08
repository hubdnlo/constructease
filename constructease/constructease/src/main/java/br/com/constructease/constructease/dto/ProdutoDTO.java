package br.com.constructease.constructease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoDTO {
    private Integer id;
    private String nome;
    private Integer quantidade;
    private Double preco;
    private String descricao;
    private Integer categoriaId;
}

