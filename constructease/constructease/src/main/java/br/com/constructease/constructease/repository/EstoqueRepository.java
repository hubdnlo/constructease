package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.exception.EstoqueNaoEncontradoException;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Repository
public class EstoqueRepository {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueRepository.class);
    private final String caminho;

    public EstoqueRepository(@Value("${estoque.repository.path:estoque.json}") String caminhoEstoque) {
        this.caminho = caminhoEstoque;
    }

    public List<Produto> buscarTodos() {
        File arquivo = new File(caminho);
        if (!arquivo.exists()) {
            logger.warn("Arquivo de estoque não encontrado: {}", caminho);
            throw new EstoqueNaoEncontradoException("Arquivo de estoque não encontrado: " + caminho);
        }

        return JsonUtil.lerJson(caminho, new TypeReference<List<Produto>>() {});
    }

    public Optional<Produto> buscarPorId(int id) {
        return buscarTodos().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public synchronized void atualizarEstoque(List<Produto> produtosAtualizados) {
        File arquivo = new File(caminho);
        File diretorioPai = arquivo.getParentFile();
        if (diretorioPai != null && !diretorioPai.exists()) {
            boolean criado = diretorioPai.mkdirs();
            if (!criado) {
                logger.error("Falha ao criar diretório: {}", diretorioPai.getAbsolutePath());
                throw new RuntimeException("Não foi possível criar o diretório para o arquivo de estoque.");
            }
        }

        JsonUtil.gravarJson(caminho, produtosAtualizados);
        logger.info("Estoque atualizado com {} produtos.", produtosAtualizados.size());
    }

    public void atualizarProduto(Produto produto) {
        List<Produto> produtos = buscarTodos();
        boolean encontrado = false;

        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produto.getId()) {
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
            throw new IllegalArgumentException("Produto com ID " + produto.getId() + " não encontrado.");
        }
    }
}
