package br.com.constructease.constructease.util;

import br.com.constructease.constructease.model.Produto;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilTest {

    @Test
    void gravarElerJson_devePersistirERecuperarArquivo() {
        String caminho = "temp_produtos.json";
        Produto p = new Produto(); p.setNome("Teste"); p.setPreco(10.0); p.setQuantidadeEstoque(5);

        JsonUtil.gravarJson(caminho, List.of(p));
        List<Produto> produtos = JsonUtil.lerJson(caminho, new com.fasterxml.jackson.core.type.TypeReference<>() {});
        assertEquals(1, produtos.size());
        new File(caminho).delete();
    }
}

