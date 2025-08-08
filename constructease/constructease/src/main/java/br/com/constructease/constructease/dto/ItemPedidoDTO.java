package br.com.constructease.constructease.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemPedidoDTO {

    @NotNull(message = "O ID do produto é obrigatório")
    @Min(value = 1, message = "O ID do produto deve ser maior que zero")
    private Integer produtoId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    private Integer quantidade;

    @Override
    public String toString() {
        return "ItemPedidoDTO{produtoId=" + produtoId + ", quantidade=" + quantidade + '}';
    }
}
