package com.algafoods.domain.repository;

import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends CustomJpaRepository<Produto, Long>{
    Optional<Produto> findByIdAndRestaurante(Long id, Restaurante restaurante);
    List<Produto> findByRestaurante(Restaurante restaurante);
}
