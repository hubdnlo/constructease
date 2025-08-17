package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.exception.EstoqueInsuficienteException;
import br.com.constructease.constructease.exception.PersistenciaEstoqueException;
import br.com.constructease.constructease.exception.ProdutoInexistenteException;
import br.com.constructease.constructease.interfaces.IEstoqueService;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.model.factory.ProdutoFactory;
import br.com.constructease.constructease.repository.EstoqueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService implements IEstoqueService {

    private final EstoqueRepository estoqueRepository;

    @Autowired
    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public List<Produto> listarItensDisponiveis() {
        return estoqueRepository.buscarTodos().stream()
                .filter(produto -> produto.getQuantidadeEstoque() > 0)
                .toList();
    }

    public Produto consultarItem(Long id) {
        return buscarProduto(id);
    }

    public Produto buscarProduto(Long produtoId) {
        return estoqueRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new ProdutoInexistenteException("Produto não encontrado: ID " + produtoId));
    }

    public double getPrecoProduto(Long produtoId) {
        Produto produto = buscarProduto(produtoId);
        if (produto.getQuantidadeEstoque() == 0) {
            throw new EstoqueInsuficienteException("Produto sem estoque disponível");
        }
        return produto.getPreco();
    }

    public boolean isDisponivel(Long produtoId, int quantidadeSolicitada) {
        Produto produto = buscarProduto(produtoId);
        return produto.getQuantidadeEstoque() >= quantidadeSolicitada;
    }

    @Transactional
    public void baixarEstoque(Long produtoId, int quantidadeBaixa) {
        Produto produto = buscarProduto(produtoId);
        if (produto.getQuantidadeEstoque() < quantidadeBaixa) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para o produto ID " + produtoId);
        }
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidadeBaixa);
        gravarProduto(produto);
    }

    @Transactional
    public void reporEstoque(Long produtoId, int quantidadeRepor) {
        Produto produto = buscarProduto(produtoId);
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidadeRepor);
        gravarProduto(produto);
    }

    /**
     * Método específico para devolução de estoque em caso de cancelamento de pedido.
     * Funcionalmente igual ao reporEstoque, mas com semântica voltada para reversão.
     */
    @Transactional
    public void devolverEstoque(Long produtoId, int quantidadeDevolvida) {
        Produto produto = buscarProduto(produtoId);
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidadeDevolvida);
        gravarProduto(produto);
    }

    @Transactional
    public void cadastrarOuAtualizarProduto(ProdutoCadastroDTO dto) {
        if (dto.getPreco() <= 0 || dto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Dados inválidos para cadastro ou atualização.");
        }

        List<Produto> produtos = estoqueRepository.buscarTodos();
        Produto existente = produtos.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(dto.getNome()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            atualizarProdutoExistente(existente, dto);
        } else {
            cadastrarNovoProduto(dto, produtos);
        }
    }

    private void atualizarProdutoExistente(Produto existente, ProdutoCadastroDTO dto) {
        int qtdAtual = existente.getQuantidadeEstoque();
        int novaQtd = dto.getQuantidade();
        double precoAtual = existente.getPreco();
        double novoPreco = dto.getPreco();

        double precoPonderado = (precoAtual * qtdAtual + novoPreco * novaQtd) / (qtdAtual + novaQtd);

        existente.setQuantidadeEstoque(qtdAtual + novaQtd);
        existente.setPreco(precoPonderado);

        gravarProduto(existente);
    }

    private void cadastrarNovoProduto(ProdutoCadastroDTO dto, List<Produto> produtos) {
        Produto novo = ProdutoFactory.criar(dto, produtos);
        produtos.add(novo);
        gravarEstoque(produtos);
    }

    @Transactional
    public void atualizarNomeProduto(Long id, String novoNome) {
        Produto produto = buscarProduto(id);
        produto.setNome(novoNome);
        gravarProduto(produto);
    }

    private void gravarProduto(Produto produto) {
        try {
            estoqueRepository.atualizarProduto(produto);
        } catch (Exception e) {
            throw new PersistenciaEstoqueException("Erro ao atualizar produto no estoque.", e);
        }
    }

    private void gravarEstoque(List<Produto> produtos) {
        try {
            estoqueRepository.atualizarEstoque(produtos);
        } catch (Exception e) {
            throw new PersistenciaEstoqueException("Erro ao atualizar lista de produtos no estoque.", e);
        }
    }
}