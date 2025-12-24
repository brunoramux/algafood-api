package com.algafoods.domain.repository.filter;

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

}
