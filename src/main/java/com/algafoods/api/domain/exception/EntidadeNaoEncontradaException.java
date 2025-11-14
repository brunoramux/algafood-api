package com.algafoods.api.domain.exception;

public class EntidadeNaoEncontradaException extends  RuntimeException{
    public EntidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}
