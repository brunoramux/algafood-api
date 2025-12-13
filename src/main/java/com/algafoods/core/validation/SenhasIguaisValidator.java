package com.algafoods.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class SenhasIguaisValidator implements ConstraintValidator<SenhasIguais, Object> {

    private String campoSenha;
    private String campoConfirmacao;

    @Override
    public void initialize(SenhasIguais constraintAnnotation) {
        this.campoSenha = constraintAnnotation.senha();
        this.campoConfirmacao = constraintAnnotation.confirmacaoSenha();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object senha = getFieldValue(value, campoSenha);
            Object confirmacao = getFieldValue(value, campoConfirmacao);

            if (senha == null || confirmacao == null) {
                return true;
            }

            return senha.equals(confirmacao);
        } catch (Exception e) {
            return false;
        }
    }

    private Object getFieldValue(Object obj, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {

        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

}
