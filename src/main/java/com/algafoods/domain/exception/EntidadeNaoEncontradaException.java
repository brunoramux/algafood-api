package com.algafoods.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serial;

//@ResponseStatus(HttpStatus.NOT_FOUND)
//public class EntidadeNaoEncontradaException extends ResponseStatusException {
//
//    @Serial
//    private static final long serialVersionUID = 1L;
//
//    public EntidadeNaoEncontradaException(HttpStatusCode status, String mensagem) {
//        super(status, mensagem);
//    }
//
//    public EntidadeNaoEncontradaException(String mensagem){
//        this(HttpStatus.NOT_FOUND, mensagem);
//    }
//}

public class EntidadeNaoEncontradaException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }

}
