package com.algafoods.infra.storage;

import com.algafoods.application.port.FotoProdutoStoragePort;
import com.algafoods.core.storage.StorageProperties;
import com.algafoods.domain.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FotoProdutoLocalStorageAdapter implements FotoProdutoStoragePort {

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public void armazenarFotoProduto(NovaFoto novaFoto) {
        try {
            Path arquivoPath = getDiretorioFotos(novaFoto.getNomeArquivo());
            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        } catch (Exception e) {
            throw new StorageException("Erro ao salvar arquivo em disco.");
        }
    }

    @Override
    public void removerFotoProduto(String nomeArquivo) {
        Path arquivoPath = getDiretorioFotos(nomeArquivo);

        try {
            Files.deleteIfExists(arquivoPath);
        } catch (IOException e) {
            throw new StorageException("Erro ao remover arquivo em disco.");
        }
    }

    @Override
    public InputStream recuperarFotoProduto(String nomeArquivo) {
        Path arquivoPath = getDiretorioFotos(nomeArquivo);

        try {
            return Files.newInputStream(arquivoPath);
        } catch (IOException e) {
            throw new StorageException("Erro ao recuperar arquivo em disco.");
        }
    }

    private Path getDiretorioFotos(String nomeArquivo) {
        return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
    }
}
