package com.algafoods.domain.repository;

import com.algafoods.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends  CustomJpaRepository<Pedido, Long> {

    Pageable pageable = PageRequest.of(
            0, // página
            10, // tamanho
            Sort.by("nome").ascending()
    );

    @Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha")
    Page<Pedido> findAll(Pageable pageable);

    Optional<Pedido> findByCodigo(String codigo);

}
