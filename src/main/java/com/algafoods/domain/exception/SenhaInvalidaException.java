package com.algafoods.domain.exception;

import java.io.Serial;

public class SenhaInvalidaException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SenhaInvalidaException(String mensagem) {
        super(mensagem);
    }

}
