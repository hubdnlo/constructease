package br.com.constructease.constructease.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;

import java.io.*;
import java.nio.file.*;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> lerJson(String caminho, TypeReference<List<T>> type) {
        try {
            return mapper.readValue(new File(caminho), type);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler JSON: " + caminho);
        }
    }

    public static <T> void gravarJson(String caminho, List<T> lista) {
        try {
            mapper.writeValue(new File(caminho), lista);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gravar JSON: " + caminho);
        }
    }

    public static <T> List<T> lerLista(String caminho, Class<T[]> clazz) {
        try {
            // Verificação de integridade do caminho
            InputStream input;
            File file = new File(caminho);
            if (file.exists()) {
                input = new FileInputStream(file);
            } else {
                input = JsonUtil.class.getClassLoader().getResourceAsStream(caminho);
                if (input == null) {
                    throw new RuntimeException("Arquivo JSON não encontrado no caminho ou classpath: " + caminho);
                }
            }
            // Conversão segura
            T[] array = mapper.readValue(input, clazz);
            return List.of(array);

        } catch (Exception e) {
            // 🪵 Log completo da exceção
            throw new RuntimeException("Erro ao ler JSON: " + caminho, e);
        }
    }

    public static <T> void gravarLista(List<T> lista, String caminho) {
        // Proteção contra lista nula ou vazia
        if (lista == null || lista.isEmpty()) {
            throw new IllegalArgumentException("Lista vazia ou nula não pode ser gravada");
        }
        try {
            //Verificação de caminho gravável
            Path path = Paths.get(caminho);
            Files.createDirectories(path.getParent());

            mapper.writeValue(path.toFile(), lista);
        } catch (IOException e) {
            //Log detalhado de erro de gravação
            throw new RuntimeException("Erro ao gravar lista em JSON: " + caminho, e);
        }
    }

}