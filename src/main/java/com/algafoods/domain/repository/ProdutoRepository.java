package com.algafoods.domain.repository;

import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface    ProdutoRepository extends CustomJpaRepository<Produto, Long>{
    Optional<Produto> findByIdAndRestaurante(Long id, Restaurante restaurante);
    List<Produto> findByRestaurante(Restaurante restaurante);

    @Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
    List<Produto> findAtivosByRestaurante(Restaurante restaurante);
}
