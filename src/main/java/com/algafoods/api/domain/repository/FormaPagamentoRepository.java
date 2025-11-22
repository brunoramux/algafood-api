package com.algafoods.api.domain.repository;

import com.algafoods.api.domain.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

public interface FormaPagamentoRepository
        extends CustomJpaRepository<FormaPagamento, Long>
{

}