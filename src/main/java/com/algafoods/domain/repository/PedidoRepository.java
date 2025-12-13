package com.algafoods.domain.repository;

import com.algafoods.domain.model.Pedido;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PedidoRepository extends  CustomJpaRepository<Pedido, Long> {}
