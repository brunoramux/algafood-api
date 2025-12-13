package com.algafoods.core.validation;

import com.algafoods.api.model.input.UsuarioInputDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhasIguaisValidator implements ConstraintValidator<SenhasIguais, UsuarioInputDTO> {

    @Override
    public boolean isValid(UsuarioInputDTO usuario, ConstraintValidatorContext context) {
        if (usuario.getSenha() == null || usuario.getConfirmacaoSenha() == null) {
            return true;
        }
        return usuario.getSenha().equals(usuario.getConfirmacaoSenha());
    }
}
