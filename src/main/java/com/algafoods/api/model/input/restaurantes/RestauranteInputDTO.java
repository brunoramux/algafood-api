package com.algafoods.api.model.input.restaurantes;

import com.algafoods.api.model.input.EnderecoInputDTO;
import com.algafoods.core.validation.Multiplo;
import com.algafoods.core.validation.TaxaFrete;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteInputDTO {

    @NotBlank
    private String nome;

    @NotNull
    @TaxaFrete
    @Multiplo(numero = 5)
    private BigDecimal taxaFrete;

    @NotNull
    private Boolean ativo;

    @Valid
    @NotNull
    private CozinhaEmCadastroRestauranteDTO cozinha;

    @Valid
    @NotNull
    private EnderecoInputDTO endereco;

}
