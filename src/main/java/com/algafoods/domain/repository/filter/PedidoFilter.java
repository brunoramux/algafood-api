package com.algafoods.domain.repository.filter;

import com.algafoods.domain.model.StatusPedido;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Setter
@Getter
public class PedidoFilter {

    private Long clienteId;

    private Long restauranteId;

    private LocalDate dataCriacaoInicio;

    private LocalDate dataCriacaoFim;

    private StatusPedido status;

}
