package br.com.constructease.constructease.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 100)
    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    @JsonManagedReference
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    private BigDecimal valorTotal;

    protected Pedido() {}

    public Pedido(String descricao) {
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusPedido.ATIVO;
    }

    public Pedido(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusPedido.ATIVO;
    }

    public Pedido(Long id, String descricao, List<ItemPedido> itens) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
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

    public BigDecimal calcularValorTotal() {
        return itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}