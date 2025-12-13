package com.algafoods.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SenhasIguaisValidator.class })
public @interface SenhasIguais {
    String message() default "{senhasIguais.invalido}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String senha();

    String confirmacaoSenha();
}
