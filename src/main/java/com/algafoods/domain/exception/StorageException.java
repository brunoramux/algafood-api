package com.algafoods.domain.exception;

public class StorageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StorageException(String message) {
        super(message);
    }
}
