package br.com.constructease.constructease.interfaces;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.model.Produto;

import java.util.List;

public interface IEstoqueService {
    List<Produto> listarItensDisponiveis();
    Produto consultarItem(int id);
    Produto buscarProduto(int produtoId);
    double getPrecoProduto(int produtoId);
    boolean isDisponivel(int produtoId, int quantidadeSolicitada);
    void baixarEstoque(int produtoId, int quantidadeBaixa);
    void reporEstoque(int produtoId, int quantidadeRepor);
    void cadastrarOuAtualizarProduto(ProdutoCadastroDTO dto);
    void atualizarNomeProduto(int id, String novoNome);
}
