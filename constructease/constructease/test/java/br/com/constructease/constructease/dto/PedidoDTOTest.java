package br.com.constructease.constructease.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PedidoDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void pedidoValido_devePassarValidacao() {
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(1);
        item.setQuantidade(2);

        PedidoDTO dto = new PedidoDTO();
        dto.setDescricao("Pedido de teste");
        dto.setItens(List.of(item));

        Set<ConstraintViolation<PedidoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void descricaoCurta_deveFalharValidacao() {
        PedidoDTO dto = new PedidoDTO();
        dto.setDescricao("abc"); // menos de 5 caracteres
        dto.setItens(List.of(new ItemPedidoDTO(1, 1)));

        Set<ConstraintViolation<PedidoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descricao")));
    }

    @Test
    void pedidoSemItens_deveFalharValidacao() {
        PedidoDTO dto = new PedidoDTO();
        dto.setDescricao("Pedido válido");
        dto.setItens(List.of()); // vazio

        Set<ConstraintViolation<PedidoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("itens")));
    }
}

