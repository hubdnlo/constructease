package br.com.constructease.constructease.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoDTO {

    @NotNull(message = "A descrição não pode ser nula")
    @Size(min = 5, max = 100, message = "A descrição deve ter entre 5 e 100 caracteres")
    private String descricao;

    @Valid
    @NotEmpty(message = "O pedido deve conter pelo menos um item")
    private List<ItemPedidoDTO> itens;

    //Ajuda muito em logs, testes unitários e depuração
    @Override
    public String toString() {
        return "PedidoDTO{descricao='" + descricao + "', itens=" + itens + '}';
    }
}