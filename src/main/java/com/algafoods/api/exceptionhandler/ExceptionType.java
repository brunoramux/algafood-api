package com.algafoods.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionType {

    ERRO_DE_SENHA_INVALIDA("/erro-de-senha-invalida", "Erro de senha inválida."),
    ERRO_DE_VALIDACAO("/erro-de-validacao", "Erro de validação."),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema."),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado."),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro Inválido."),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível."),
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada."),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso.");

    private final String title;
    private final String uri;

    ExceptionType(String path, String title){
        this.title = title;
        this.uri = "https://algafoods.com.br" + path;
    }
}
