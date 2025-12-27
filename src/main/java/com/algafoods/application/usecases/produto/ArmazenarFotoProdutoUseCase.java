package com.algafoods.application.usecases.produto;

import com.algafoods.application.port.FotoProdutoStoragePort;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ArmazenarFotoProdutoUseCase {
    private final FotoProdutoStoragePort fotoProdutoStorage;

    public ArmazenarFotoProdutoUseCase(FotoProdutoStoragePort fotoProdutoStorage) {
        this.fotoProdutoStorage = fotoProdutoStorage;
    }

    public void execute(InputStream inputStream, String nomeArquivo) {
        fotoProdutoStorage.armazenarFotoProduto(
                FotoProdutoStoragePort.NovaFoto
                        .builder()
                        .nomeArquivo(nomeArquivo)
                        .inputStream(inputStream)
                        .build()
        );
    }
}
