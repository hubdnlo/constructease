package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.exception.EstoqueInsuficienteException;
import br.com.constructease.constructease.exception.ProdutoInexistenteException;
import br.com.constructease.constructease.interfaces.IEstoqueService;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService implements IEstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    public List<Produto> listarItensDisponiveis() {
        return estoqueRepository.buscarTodos()
                .stream()
                .filter(produto -> produto.getQuantidadeEstoque() > 0)
                .toList();
    }

    public Produto consultarItem(int id) {
        return buscarProduto(id); // Reuso do método já existente que lança exception
    }

    /**
     * Busca um produto por ID e lança exceção customizada se não for encontrado.
     */
    public Produto buscarProduto(int produtoId) {
        return estoqueRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new ProdutoInexistenteException("Produto não encontrado: ID " + produtoId));
    }

    /**
     * Obtém o preço atual do produto.
     */
    public double getPrecoProduto(int produtoId) {
        Produto produto = buscarProduto(produtoId);
        if (produto.getQuantidadeEstoque() == 0) {
            throw new EstoqueInsuficienteException("Produto sem estoque disponível");
        }
        return produto.getPreco();
    }
    /**
     * Verifica se o produto possui estoque suficiente para a quantidade solicitada.
     */
    public boolean isDisponivel(int produtoId, int quantidadeSolicitada) {
        Produto produto = buscarProduto(produtoId);
        return produto.getQuantidadeEstoque() >= quantidadeSolicitada;
    }

    /**
     * Baixa a quantidade do estoque do produto.
     */
    public void baixarEstoque(int produtoId, int quantidadeBaixa) {
        List<Produto> produtos = estoqueRepository.buscarTodos();
        Produto produto = buscarProduto(produtoId);

        if (produto.getQuantidadeEstoque() < quantidadeBaixa) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para o produto ID " + produtoId);
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidadeBaixa);
        estoqueRepository.atualizarEstoque(produtos);
    }

    /**
     * Recompõe a quantidade de estoque do produto.
     */
    public void reporEstoque(int produtoId, int quantidadeRepor) {
        List<Produto> produtos = estoqueRepository.buscarTodos();
        Produto produto = buscarProduto(produtoId);

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidadeRepor);
        estoqueRepository.atualizarEstoque(produtos);
    }

    public void cadastrarOuAtualizarProduto(ProdutoCadastroDTO dto) {
        List<Produto> produtos = estoqueRepository.buscarTodos();

        Produto existente = produtos.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(dto.getNome()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            if (dto.getPreco() <= 0) {
                throw new IllegalArgumentException("Preço inválido para atualização.");
            }

            int qtdAtual = existente.getQuantidadeEstoque();
            int novaQtd = dto.getQuantidade();
            double precoAtual = existente.getPreco();
            double novoPreco = dto.getPreco();

            double precoPonderado = (precoAtual * qtdAtual + novoPreco * novaQtd) / (qtdAtual + novaQtd);

            existente.setQuantidadeEstoque(qtdAtual + novaQtd);
            existente.setPreco(precoPonderado);

            estoqueRepository.atualizarProduto(existente);
        } else {
            Produto novo = new Produto();
            novo.setNome(dto.getNome());
            novo.setQuantidadeEstoque(dto.getQuantidade());
            novo.setPreco(dto.getPreco());

            produtos.add(novo);
            estoqueRepository.atualizarEstoque(produtos);
        }
    }

    public void atualizarNomeProduto(int id, String novoNome) {
        Produto produto = buscarProduto(id);
        produto.setNome(novoNome);
        estoqueRepository.atualizarProduto(produto);
    }


}