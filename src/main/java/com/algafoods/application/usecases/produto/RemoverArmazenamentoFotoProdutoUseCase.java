package com.algafoods.application.usecases.produto;

import com.algafoods.application.port.FotoProdutoStoragePort;
import org.springframework.stereotype.Service;


@Service
public class RemoverArmazenamentoFotoProdutoUseCase {
    private final FotoProdutoStoragePort fotoProdutoStorage;

    public RemoverArmazenamentoFotoProdutoUseCase(FotoProdutoStoragePort fotoProdutoStorage) {
        this.fotoProdutoStorage = fotoProdutoStorage;
    }

    public void execute(String nomeArquivo) {
        fotoProdutoStorage.removerFotoProduto(nomeArquivo);
    }
}
