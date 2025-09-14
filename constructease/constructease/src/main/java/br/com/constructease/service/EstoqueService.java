package br.com.constructease.service;

import br.com.constructease.dto.ProdutoCadastroDTO;
import br.com.constructease.exception.EstoqueInsuficienteException;
import br.com.constructease.exception.PersistenciaEstoqueException;
import br.com.constructease.exception.ProdutoInexistenteException;
import br.com.constructease.interfaces.IEstoqueService;
import br.com.constructease.model.Produto;
import br.com.constructease.model.factory.ProdutoFactory;
import br.com.constructease.repository.EstoqueArquivoRepository;
import br.com.constructease.util.FormatadorDecimal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EstoqueService implements IEstoqueService {

    private final EstoqueArquivoRepository estoqueArquivoRepository;

    @Autowired
    public EstoqueService(EstoqueArquivoRepository estoqueArquivoRepository) {
        this.estoqueArquivoRepository = estoqueArquivoRepository;
    }

    public List<Produto> listarItensDisponiveis() {
        return estoqueArquivoRepository.buscarTodos().stream()
                .filter(produto -> produto.getQuantidade() > 0)
                .toList();
    }

    public Produto consultarItem(Long id) {
        return buscarProduto(id);
    }

    public Produto buscarProduto(Long produtoId) {
        return estoqueArquivoRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new ProdutoInexistenteException("Produto não encontrado: ID " + produtoId));
    }

    public BigDecimal getPrecoProduto(Long produtoId) {
        Produto produto = buscarProduto(produtoId);
        if (produto.getQuantidade() == 0) {
            throw new EstoqueInsuficienteException("Produto sem estoque disponível");
        }
        return FormatadorDecimal.arredondar(produto.getPreco());
    }

    public String getNomeProduto(Long produtoId) {
        Produto produto = buscarProduto(produtoId);
        return produto.getNome();
    }

    public boolean isDisponivel(Long produtoId, int quantidadeSolicitada) {
        Produto produto = buscarProduto(produtoId);
        return produto.getQuantidade() >= quantidadeSolicitada;
    }

    @Transactional
    public void baixarEstoque(Long produtoId, int quantidadeBaixa) {
        Produto produto = buscarProduto(produtoId);
        if (produto.getQuantidade() < quantidadeBaixa) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para o produto ID " + produtoId);
        }
        produto.setQuantidade(produto.getQuantidade() - quantidadeBaixa);
        gravarProduto(produto);
    }

    @Transactional
    public void reporEstoque(Long produtoId, int quantidadeRepor) {
        Produto produto = buscarProduto(produtoId);
        produto.setQuantidade(produto.getQuantidade() + quantidadeRepor);
        gravarProduto(produto);
    }

    @Transactional
    public void devolverEstoque(Long produtoId, int quantidadeDevolvida) {
        Produto produto = buscarProduto(produtoId);
        produto.setQuantidade(produto.getQuantidade() + quantidadeDevolvida);
        gravarProduto(produto);
    }

    @Transactional
    public void cadastrarOuAtualizarProduto(ProdutoCadastroDTO dto) {
        if (dto.getPreco().compareTo(BigDecimal.ZERO) <= 0 || dto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Dados inválidos para cadastro ou atualização.");
        }

        List<Produto> produtos = estoqueArquivoRepository.buscarTodos();

        Produto existente = produtos.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(dto.getNome()) &&
                        p.getDescricao().equalsIgnoreCase(dto.getDescricao()) &&
                        p.getCategoriaId().equals(dto.getCategoriaId()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            atualizarProdutoExistente(existente, dto);
        } else {
            cadastrarNovoProduto(dto, produtos);
        }
    }

    private void atualizarProdutoExistente(Produto existente, ProdutoCadastroDTO dto) {
        int qtdAtual = existente.getQuantidade();
        int novaQtd = dto.getQuantidade();
        BigDecimal precoAtual = existente.getPreco();
        BigDecimal novoPreco = dto.getPreco();

        BigDecimal totalAtual = precoAtual.multiply(BigDecimal.valueOf(qtdAtual));
        BigDecimal totalNovo = novoPreco.multiply(BigDecimal.valueOf(novaQtd));
        BigDecimal precoPonderado = totalAtual.add(totalNovo)
                .divide(BigDecimal.valueOf(qtdAtual + novaQtd), 2, BigDecimal.ROUND_HALF_UP);

        existente.setQuantidade(qtdAtual + novaQtd);
        existente.setPreco(FormatadorDecimal.arredondar(precoPonderado));

        gravarProduto(existente);
    }

    private void cadastrarNovoProduto(ProdutoCadastroDTO dto, List<Produto> produtos) {
        Produto novo = ProdutoFactory.criar(dto, produtos);
        novo.setPreco(FormatadorDecimal.arredondar(novo.getPreco()));
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
            estoqueArquivoRepository.atualizarProduto(produto);
        } catch (Exception e) {
            throw new PersistenciaEstoqueException("Erro ao atualizar produto no estoque.", e);
        }
    }

    private void gravarEstoque(List<Produto> produtos) {
        try {
            estoqueArquivoRepository.atualizarEstoque(produtos);
        } catch (Exception e) {
            throw new PersistenciaEstoqueException("Erro ao atualizar lista de produtos no estoque.", e);
        }
    }
}