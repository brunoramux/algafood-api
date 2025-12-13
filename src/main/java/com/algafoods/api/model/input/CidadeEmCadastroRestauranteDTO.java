package com.algafoods.api.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeEmCadastroRestauranteDTO {
    @NotNull
    private Long id;
}
