package br.com.constructease.constructease.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Positive(message = "A quantidade deve ser positiva")
    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Positive(message = "O preço deve ser positivo")
    @Column(name = "preco", nullable = false)
    private double preco;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidade;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidade = quantidadeEstoque;
    }

    @Override
    public String toString() {
        return "Produto{id=" + id + ", nome='" + nome + '\'' +
                ", quantidade=" + quantidade + ", preco=" + preco + '}';
    }

    public void setDescricao(String areiaParaAcabamento) {
    }

    public void setCategoriaId(int i) {
    }
}