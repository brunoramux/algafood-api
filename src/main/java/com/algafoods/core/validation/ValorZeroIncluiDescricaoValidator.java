package com.algafoods.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {

    private String valorField;
    private String descricaoField;
    private String descricaoObrigatoria;

    @Override
    public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
        this.valorField = constraintAnnotation.valorField();
        this.descricaoField = constraintAnnotation.descricaoField();
        this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean valido = true;

        try {
            BigDecimal valor = (BigDecimal) Objects.requireNonNull(BeanUtils.getPropertyDescriptor(o.getClass(), valorField)).getReadMethod().invoke(o);
            String descricao = (String) Objects.requireNonNull(BeanUtils.getPropertyDescriptor(o.getClass(), descricaoField)).getReadMethod().invoke(o);

            if(valor.compareTo(BigDecimal.ZERO) == 0){
                valido = descricao.contains(descricaoObrigatoria);
            }
        } catch (Exception e) {
            throw new ValidationException(e);
        }

        return valido;
    }
}
