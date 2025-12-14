package com.algafoods.api.model.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoInputDTO {

    @NotNull
    private String nome;

    @NotNull
    private String descricao;

    @NotNull
    @PositiveOrZero
    private BigDecimal preco;

    @NotNull
    private boolean ativo;

}
