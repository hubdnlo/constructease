package br.com.constructease.constructease.interfaces;

import br.com.constructease.constructease.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNomeIgnoreCase(String nome);

    Optional<Produto> findByNomeIgnoreCaseAndDescricaoIgnoreCaseAndCategoriaId(
            String nome,
            String descricao,
            Integer categoriaId
    );
}