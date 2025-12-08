package com.algafoods.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValorZeroIncluiDescricaoValidator.class })
public @interface ValorZeroIncluiDescricao {
    String message() default "{Nome.ValorZeroIncluiDescricao}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

     String valorField();

     String descricaoField();

     String descricaoObrigatoria();
}
