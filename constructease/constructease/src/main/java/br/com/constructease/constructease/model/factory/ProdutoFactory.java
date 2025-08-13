package br.com.constructease.constructease.model.factory;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.model.Produto;

import java.io.IOException;
import java.util.List;

/**
 * Fábrica responsável por criar instâncias de Produto com ID gerado automaticamente.
 */
public class ProdutoFactory {

    /**
     * Cria um novo produto com base nos dados do DTO.
     * O ID é gerado de forma persistente usando o IdGenerator.
     *
     * @param dto Dados recebidos para cadastro.
     * @return Produto com ID único e dados preenchidos.
     */
    public static Produto criar(ProdutoCadastroDTO dto, List<Produto> produtosExistentes) {
        Long novoId = gerarProximoId(produtosExistentes);

        Produto produto = new Produto();
        produto.setId(novoId);
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setQuantidadeEstoque(dto.getQuantidade());

        return produto;
    }

    private static Long gerarProximoId(List<Produto> produtos) {
        return produtos.stream()
                .mapToLong(p -> p.getId() != null ? p.getId() : 0)
                .max()
                .orElse(0L) + 1;
    }
}