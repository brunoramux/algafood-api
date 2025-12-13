package com.algafoods.api.model.input;

import com.algafoods.api.model.EnderecoModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteInputDTO {

    @NotBlank
    private String nome;

    @NotNull
    @PositiveOrZero
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
