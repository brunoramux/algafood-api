package com.algafoods.application.filter;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VendaDiariaFilter {

    private Long restauranteId;

    private LocalDate dataCriacaoInicio;

    private LocalDate dataCriacaoFim;

}
