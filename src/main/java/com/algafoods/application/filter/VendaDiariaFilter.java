package com.algafoods.application.filter;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class VendaDiariaFilter {

    private Long restauranteId;

    private LocalDate dataCriacaoInicio;

    private LocalDate dataCriacaoFim;

}
