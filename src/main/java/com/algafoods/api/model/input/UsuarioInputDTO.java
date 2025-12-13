package com.algafoods.api.model.input;


import com.algafoods.api.model.GrupoModel;
import com.algafoods.core.validation.SenhasIguais;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@SenhasIguais(
        senha = "senha",
        confirmacaoSenha = "confirmacaoSenha"
)
public class UsuarioInputDTO {

    @NotNull
    private String nome;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "A senha deve ter no mínimo 8 caracteres, uma letra maiúscula, uma minúscula, um número e um caractere especial"
    )
    private String senha;

    @NotNull
    private String confirmacaoSenha;

    @Valid
    @NotNull
    private List<GrupoEmCadastroUsuarioDTO> grupos;

}
