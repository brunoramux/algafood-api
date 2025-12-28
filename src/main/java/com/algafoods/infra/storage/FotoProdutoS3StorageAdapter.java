package com.algafoods.infra.storage;

import com.algafoods.application.port.FotoProdutoStoragePort;
import com.algafoods.core.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

public class FotoProdutoS3StorageAdapter implements FotoProdutoStoragePort {

    @Autowired
    private StorageProperties storageProperties;

    @Autowired
    private S3Client s3;

    @Override
    public void armazenarFotoProduto(NovaFoto novaFoto) {

        s3.putObject(
                PutObjectRequest
                        .builder()
                        .bucket(storageProperties.getS3().getBucketName())
                        .key(storageProperties.getS3().getFotosDir() + "/" + novaFoto.getNomeArquivo())

                        .build(),
                RequestBody.fromInputStream(novaFoto.getInputStream(), novaFoto.getTamanhoArquivo())
        );

    }

    @Override
    public void removerFotoProduto(String nomeArquivo) {

    }

    @Override
    public InputStream recuperarFotoProduto(String nomeArquivo) {
        System.out.println("Recuperando foto do produto: " + storageProperties.getS3().getFotosDir() + "/" + nomeArquivo);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(storageProperties.getS3().getBucketName())
                .key(storageProperties.getS3().getFotosDir() + "/" + nomeArquivo)
                .build();

        return s3.getObject(getObjectRequest);
    }
}
