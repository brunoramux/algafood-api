package com.algafoods.application.port;

import com.algafoods.domain.model.FotoProduto;
import com.algafoods.domain.repository.CustomJpaRepository;

import java.util.Optional;

public interface FotoProdutoPort extends CustomJpaRepository<FotoProduto, Long> {

    Optional<FotoProduto> findById(Long produtoId);

}
