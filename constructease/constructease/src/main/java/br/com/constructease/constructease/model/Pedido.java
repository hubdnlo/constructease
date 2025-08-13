package br.com.constructease.constructease.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 100)
    private String descricao;

    private LocalDateTime dataCriacao;

    private boolean ativo;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    protected Pedido() {}

    public Pedido(String descricao) {
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.status = StatusPedido.ATIVO;
    }

    public Pedido(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.status = StatusPedido.ATIVO;
    }

    public Pedido(Long id, String descricao, List<ItemPedido> itens) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.status = StatusPedido.ATIVO;
        setItens(itens);
    }

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens.clear();
        for (ItemPedido item : itens) {
            item.setPedido(this);
            this.itens.add(item);
        }
    }

    public double getValorTotal() {
        return itens.stream()
                .mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade())
                .sum();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", ativo=" + ativo +
                ", status=" + status +
                ", itens=" + itens +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }
}