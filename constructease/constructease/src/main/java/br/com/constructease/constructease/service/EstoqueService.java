package br.com.constructease.constructease.service;

import br.com.constructease.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.constructease.exception.EstoqueInsuficienteException;
import br.com.constructease.constructease.exception.PersistenciaEstoqueException;
import br.com.constructease.constructease.exception.ProdutoInexistenteException;
import br.com.constructease.constructease.interfaces.IEstoqueService;
import br.com.constructease.constructease.model.Produto;
import br.com.constructease.constructease.repository.EstoqueRepository;
import br.com.constructease.constructease.repository.PedidoRepository;
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

    public Produto consultarItem(int id) {
        return buscarProduto(id);
    }

    public Produto buscarProduto(int produtoId) {
        return estoqueRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new ProdutoInexistenteException("Produto não encontrado: ID " + produtoId));
    }

    public double getPrecoProduto(int produtoId) {
        Produto produto = buscarProduto(produtoId);
        if (produto.getQuantidadeEstoque() == 0) {
            throw new EstoqueInsuficienteException("Produto sem estoque disponível");
        }
        return produto.getPreco();
    }

    public boolean isDisponivel(int produtoId, int quantidadeSolicitada) {
        Produto produto = buscarProduto(produtoId);
        return produto.getQuantidadeEstoque() >= quantidadeSolicitada;
    }

    public void baixarEstoque(int produtoId, int quantidadeBaixa) {
        Produto produto = buscarProduto(produtoId);
        if (produto.getQuantidadeEstoque() < quantidadeBaixa) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para o produto ID " + produtoId);
        }
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidadeBaixa);
        gravarProduto(produto);
    }

    public void reporEstoque(int produtoId, int quantidadeRepor) {
        Produto produto = buscarProduto(produtoId);
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidadeRepor);
        gravarProduto(produto);
    }

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
        Produto novo = new Produto();
        novo.setNome(dto.getNome());
        novo.setQuantidadeEstoque(dto.getQuantidade());
        novo.setPreco(dto.getPreco());

        produtos.add(novo);
        gravarEstoque(produtos);
    }

    public void atualizarNomeProduto(int id, String novoNome) {
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
