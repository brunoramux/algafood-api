package com.algafoods.api.model.input;

import com.algafoods.core.validation.SenhasIguais;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SenhasIguais(
        senha = "novaSenha",
        confirmacaoSenha = "confirmacaoNovaSenha"
)
public class AlteracaoSenhaInputDTO {

    private String senhaAtual;

    @NotNull
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "A senha deve ter no mínimo 8 caracteres, uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    private String novaSenha;

    private String confirmacaoNovaSenha;

}
