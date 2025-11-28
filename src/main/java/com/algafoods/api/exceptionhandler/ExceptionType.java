package com.algafoods.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionType {

    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso");

    private String title;
    private String uri;

    ExceptionType(String path, String title){
        this.title = title;
        this.uri = "https://algafoods.com.br" + path;
    }
}
