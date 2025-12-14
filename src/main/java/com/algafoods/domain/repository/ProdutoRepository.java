package com.algafoods.domain.repository;

import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;

import java.util.List;

public interface ProdutoRepository extends CustomJpaRepository<Produto, Long>{
    List<Produto> findByRestaurante(Restaurante restaurante);
}
