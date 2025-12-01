package com.algafoods.api.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.PositiveOrZero;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { })
@PositiveOrZero
public @interface TaxaFrete {

    @OverridesAttribute(constraint = PositiveOrZero.class, name = "message")
    String message() default "{TaxaFrete.invalido}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
