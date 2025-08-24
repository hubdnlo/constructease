package br.com.constructease.constructease.interfaces;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.model.Produto;

import java.math.BigDecimal;
import java.util.List;

public interface IEstoqueService {

    List<Produto> listarItensDisponiveis();

    Produto consultarItem(Long id);

    Produto buscarProduto(Long produtoId);

    BigDecimal getPrecoProduto(Long produtoId);

    String getNomeProduto(Long produtoId);

    boolean isDisponivel(Long produtoId, int quantidadeSolicitada);

    void baixarEstoque(Long produtoId, int quantidadeBaixa);

    void reporEstoque(Long produtoId, int quantidadeRepor);

    void devolverEstoque(Long produtoId, int quantidadeDevolvida);

    void cadastrarOuAtualizarProduto(ProdutoCadastroDTO dto);

    void atualizarNomeProduto(Long id, String novoNome);
}