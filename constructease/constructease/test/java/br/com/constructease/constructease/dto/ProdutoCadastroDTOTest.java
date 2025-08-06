package br.com.constructease.constructease.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoCadastroDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void produtoValido_devePassarValidacao() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO();
        dto.setNome("Cimento CP-II");
        dto.setDescricao("Cimento de alta resistência para construção civil.");
        dto.setPreco(BigDecimal.valueOf(29.90).doubleValue());
        dto.setCategoriaId(1);

        Set<ConstraintViolation<ProdutoCadastroDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nomeEmBranco_deveFalharValidacao() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO();
        dto.setNome("   ");
        dto.setDescricao("Descrição válida");
        dto.setPreco(BigDecimal.valueOf(10.0).doubleValue());
        dto.setCategoriaId(1);

        Set<ConstraintViolation<ProdutoCadastroDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void precoNegativo_deveFalharValidacao() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO();
        dto.setNome("Produto");
        dto.setDescricao("Descrição válida");
        dto.setPreco(BigDecimal.valueOf(-5.0).doubleValue());
        dto.setCategoriaId(1);

        Set<ConstraintViolation<ProdutoCadastroDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("preco")));
    }

    @Test
    void categoriaNula_deveFalharValidacao() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO();
        dto.setNome("Produto");
        dto.setDescricao("Descrição válida");
        dto.setPreco(BigDecimal.valueOf(15.0).doubleValue());
        dto.setCategoriaId(null);

        Set<ConstraintViolation<ProdutoCadastroDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("categoriaId")));
    }
}

