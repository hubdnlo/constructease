package br.com.constructease.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para FormatadorDecimal")
class FormatadorDecimalTest {

    @Test
    @DisplayName("Deve arredondar valor para duas casas decimais com HALF_UP")
    void arredondarValor() {
        BigDecimal valor = new BigDecimal("10.456");
        BigDecimal resultado = FormatadorDecimal.arredondar(valor);

        assertEquals(new BigDecimal("10.46"), resultado);
    }

    @Test
    @DisplayName("Deve manter valor já arredondado")
    void valorJaArredondado() {
        BigDecimal valor = new BigDecimal("5.00");
        BigDecimal resultado = FormatadorDecimal.arredondar(valor);

        assertEquals(new BigDecimal("5.00"), resultado);
    }

    @Test
    @DisplayName("Deve instanciar FormatadorDecimal via reflexão para cobrir construtor")
    void instanciarConstrutorPrivadoViaReflexao() throws Exception {
        Constructor<FormatadorDecimal> constructor = FormatadorDecimal.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        FormatadorDecimal instancia = constructor.newInstance();

        assertNotNull(instancia);
    }
}