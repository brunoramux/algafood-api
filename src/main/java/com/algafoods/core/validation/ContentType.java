package com.algafoods.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ContentTypeValidator.class})
public @interface ContentType {

    String message() default "{tipo do arquivo inválido.}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowedMediaTypes();

}
