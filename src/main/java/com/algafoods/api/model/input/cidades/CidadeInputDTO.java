package com.algafoods.api.model.input.cidades;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeInputDTO {

    @NotNull
    private String nome;

    @Valid
    @NotNull
    private EstadoEmCidadeInputDTO estado;

}
