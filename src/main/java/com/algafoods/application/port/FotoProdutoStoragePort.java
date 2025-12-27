package com.algafoods.application.port;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;

public interface FotoProdutoStoragePort {

    void armazenarFotoProduto(NovaFoto novaFoto);

    @Getter
    @Builder
    class NovaFoto {

        private String nomeArquivo;
        private InputStream inputStream;

    }

}
