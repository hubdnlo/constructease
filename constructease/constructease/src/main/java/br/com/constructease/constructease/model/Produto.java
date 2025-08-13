package br.com.constructease.constructease.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Positive(message = "A quantidade deve ser positiva")
    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Positive(message = "O preço deve ser positivo")
    @Column(name = "preco", nullable = false)
    private double preco;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidadeEstoque() {
        return quantidade;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidade = quantidadeEstoque;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(Long novoId) {
        this.id = novoId;
    }
}