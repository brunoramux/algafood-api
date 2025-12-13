package com.algafoods.api.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoEmCadastroUsuarioDTO {
    @NotNull
    private Long id;
}
