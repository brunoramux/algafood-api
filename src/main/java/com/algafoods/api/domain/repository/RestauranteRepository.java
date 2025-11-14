package com.algafoods.api.domain.repository;

import com.algafoods.api.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

    Optional<Restaurante> findFirstByNomeContaining(String nome);
    // TODO

    List<Restaurante> findTop5ByNomeContaining(String nome);
    // TODO

    boolean existsByNome(String nome);
    // TODO

    int countByCozinhaId(Long cozinhaId);
    // TODO
}
