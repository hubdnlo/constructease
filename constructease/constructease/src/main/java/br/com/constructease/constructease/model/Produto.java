package br.com.constructease.constructease.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Entidade que representa um produto disponível no sistema.
 */
@Data
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "A descrição do produto é obrigatória")
    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @NotNull(message = "A categoria do produto é obrigatória")
    @Column(name = "categoria_id", nullable = false)
    private Integer categoriaId;

    @Positive(message = "A quantidade deve ser positiva")
    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Positive(message = "O preço deve ser positivo")
    @Column(name = "preco", nullable = false)
    private double preco;

    public Produto() {
        // Construtor protegido para JPA
    }

    public Produto(String nome, String descricao, Integer categoriaId, int quantidade, double preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoriaId = categoriaId;
        this.quantidade = quantidade;
        this.preco = preco;
    }
}