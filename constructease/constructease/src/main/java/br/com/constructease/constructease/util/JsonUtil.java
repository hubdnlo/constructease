package br.com.constructease.constructease.util;

import br.com.constructease.constructease.exception.JsonGravacaoException;
import br.com.constructease.constructease.exception.JsonLeituraException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Lê um JSON genérico de um caminho e o converte para o tipo especificado.
     */
    public static <T> T lerJson(String caminho, TypeReference<T> type) {
        try (InputStream is = abrirArquivo(caminho)) {
            logger.info("Lendo JSON de {}", caminho);
            return mapper.readValue(is, type);
        } catch (IOException e) {
            logger.error("Erro ao ler JSON de {}", caminho, e);
            throw new JsonLeituraException("Erro ao ler JSON: " + caminho, e);
        }
    }

    /**
     * Grava uma lista genérica como JSON no caminho especificado.
     */
    public static <T> void gravarJson(String caminho, List<T> lista) {
        try {
            Path path = Paths.get(caminho);
            criarDiretorioSeNecessario(path);
            logger.info("Gravando JSON em {} com {} itens", caminho, lista.size());
            mapper.writeValue(path.toFile(), lista);
        } catch (IOException e) {
            logger.error("Erro ao gravar JSON em {}", caminho, e);
            throw new JsonGravacaoException("Erro ao gravar JSON: " + caminho, e);
        }
    }

    /**
     * Lê uma lista de objetos de um JSON.
     */
    public static <T> List<T> lerLista(String caminho, Class<T[]> clazz) {
        try (InputStream input = abrirArquivo(caminho)) {
            logger.info("Lendo lista JSON de {}", caminho);
            T[] array = mapper.readValue(input, clazz);
            return List.of(array);
        } catch (IOException e) {
            logger.error("Erro ao ler lista JSON de {}", caminho, e);
            throw new JsonLeituraException("Erro ao ler lista JSON: " + caminho, e);
        }
    }

    /**
     * Abre um arquivo JSON de forma segura, seja por caminho absoluto ou classpath.
     */
    private static InputStream abrirArquivo(String caminho) throws IOException {
        File file = new File(caminho);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        InputStream is = JsonUtil.class.getClassLoader().getResourceAsStream(caminho);
        if (is == null) {
            throw new FileNotFoundException("Arquivo JSON não encontrado: " + caminho);
        }
        return is;
    }

    /**
     * Cria diretório pai se necessário.
     */
    private static void criarDiretorioSeNecessario(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}
