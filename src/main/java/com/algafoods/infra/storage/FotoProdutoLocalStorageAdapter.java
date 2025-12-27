package com.algafoods.infra.storage;

import com.algafoods.application.port.FotoProdutoStoragePort;
import com.algafoods.domain.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FotoProdutoLocalStorageAdapter implements FotoProdutoStoragePort {

    @Value("${algafood.storage.local.diretorio-fotos}")
    private Path diretorioFotos;

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

    private Path getDiretorioFotos(String nomeArquivo) {
        return diretorioFotos.resolve(Path.of(nomeArquivo));
    }
}
