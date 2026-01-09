package com.algafoods.api.model.input.cidades;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoEmCidadeInputDTO {

    @NotNull
    private Long id;

}
