package com.algafoods.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class ContentTypeValidator implements ConstraintValidator<ContentType, MultipartFile> {

    private String[] allowedContentTypes;

    @Override
    public void initialize(ContentType constraintAnnotation) {
        this.allowedContentTypes = constraintAnnotation.allowedMediaTypes();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;

        var fileType = multipartFile.getContentType();

        if(!Arrays.asList(allowedContentTypes).contains(fileType)) {
            valid = false;
        }

        return valid;
    }
}
