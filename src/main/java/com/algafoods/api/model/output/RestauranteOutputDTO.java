package com.algafoods.api.model.output;

import com.algafoods.api.model.CozinhaModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteOutputDTO {

    private Long id;

    private String nome;

    private BigDecimal taxaFrete;

    private CozinhaModel cozinha;

}
