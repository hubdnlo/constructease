package br.com.constructease.constructease.repository;

import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class EstoqueRepository {
    private final String caminho = "estoque.json";

    public List<Produto> buscarTodos() {
        return JsonUtil.lerJson(caminho, new TypeReference<List<Produto>>() {});
    }

    public Optional<Produto> buscarPorId(int id) {
        return buscarTodos().stream().filter(p -> p.getId() == id).findFirst();
    }

    public void atualizarEstoque(List<Produto> produtosAtualizados) {
        JsonUtil.gravarJson(caminho, produtosAtualizados);
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
        } else {
            System.err.println("Produto com ID " + produto.getId() + " não encontrado.");
        }
    }

}