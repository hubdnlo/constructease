package br.com.constructease.constructease.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {

    @Valid
    @NotNull(message = "O ID do produto é obrigatório")
    @Min(value = 1, message = "O ID do produto deve ser maior que zero")
    private Integer produtoId;

    @Valid
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    private Integer quantidade;

    @Override
    public String toString() {
        return "ItemPedidoDTO{produtoId=" + produtoId + ", quantidade=" + quantidade + '}';
    }
}
