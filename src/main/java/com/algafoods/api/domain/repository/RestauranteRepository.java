package com.algafoods.api.domain.repository;

import com.algafoods.api.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RestauranteRepository
        extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries,
        JpaSpecificationExecutor<Restaurante>
{
    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

    //@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
    List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);

    Optional<Restaurante> findFirstByNomeContaining(String nome);
    // TODO

    List<Restaurante> findTop5ByNomeContaining(String nome);
    // TODO

    boolean existsByNome(String nome);
    // TODO

    int countByCozinhaId(Long cozinhaId);
    // TODO
}
