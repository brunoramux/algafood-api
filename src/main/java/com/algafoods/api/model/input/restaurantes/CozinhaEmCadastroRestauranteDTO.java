package com.algafoods.api.model.input.restaurantes;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CozinhaEmCadastroRestauranteDTO {

    @NotNull
    private Long id;

}
