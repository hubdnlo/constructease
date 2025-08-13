package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.exception.EstoqueNaoEncontradoException;
import br.com.constructease.constructease.exception.PersistenciaEstoqueException;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Repository
public class EstoqueRepository {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueRepository.class);

    private final Resource estoqueJson;

    @Autowired
    public EstoqueRepository(@Value("${estoque.repository.path}") Resource resource) {
        this.estoqueJson = resource;
    }

    public List<Produto> buscarTodos() {
        try (InputStream input = estoqueJson.getInputStream()) {
            return JsonUtil.lerJson(input, new TypeReference<List<Produto>>() {});
        } catch (IOException e) {
            logger.warn("Erro ao ler arquivo de estoque: {}", estoqueJson.getFilename(), e);
            throw new EstoqueNaoEncontradoException("Erro ao ler arquivo de estoque: " + estoqueJson.getFilename());
        }
    }

    public Optional<Produto> buscarPorId(Long id) {
        return buscarTodos().stream()
                .filter(p -> p.getId().equals(id)) // Usar equals para comparar Long
                .findFirst();
    }

    public synchronized void atualizarEstoque(List<Produto> produtosAtualizados) {
        try {
            String caminhoFisico = estoqueJson.getFile().getPath();
            JsonUtil.gravarJson(caminhoFisico, produtosAtualizados);
            logger.info("Estoque atualizado com {} produtos.", produtosAtualizados.size());
        } catch (IOException e) {
            logger.error("Erro ao gravar estoque no arquivo: {}", estoqueJson.getFilename(), e);
            throw new PersistenciaEstoqueException("Erro ao gravar estoque no arquivo: " + estoqueJson.getFilename(), e);
        }
    }

    public void atualizarProduto(Produto produto) {
        List<Produto> produtos = buscarTodos();
        boolean encontrado = false;

        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId().equals(produto.getId())) {
                produtos.set(i, produto);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            atualizarEstoque(produtos);
            logger.info("Produto com ID {} atualizado com sucesso.", produto.getId());
        } else {
            logger.warn("Produto com ID {} não encontrado para atualização.", produto.getId());
            throw new EstoqueNaoEncontradoException("Produto com ID " + produto.getId() + " não encontrado no estoque.");
        }
    }
}