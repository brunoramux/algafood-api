package com.algafoods.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private String max;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        boolean valido = true;

        var maxFileSize = parseToBytes(this.max);
        var fileSize = multipartFile.getSize();

        if (fileSize > maxFileSize) {
            valido = false;
        }
        return valido;
    }


    public static long parseToBytes(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Valor inválido");
        }

        String normalized = value.trim().toUpperCase();

        long multiplier = 1;

        if (normalized.endsWith("KB")) {
            multiplier = 1024;
            normalized = normalized.replace("KB", "");
        } else if (normalized.endsWith("MB")) {
            multiplier = 1024 * 1024;
            normalized = normalized.replace("MB", "");
        } else if (normalized.endsWith("GB")) {
            multiplier = 1024L * 1024L * 1024L;
            normalized = normalized.replace("GB", "");
        } else if (normalized.endsWith("B")) {
            normalized = normalized.replace("B", "");
        } else {
            throw new IllegalArgumentException("Unidade inválida, informe KB, MB, GB ou B");
        }

        long number = Long.parseLong(normalized.trim());
        return number * multiplier;
    }
}
