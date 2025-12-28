package com.algafoods.core.storage;

import com.algafoods.application.port.FotoProdutoStoragePort;
import com.algafoods.infra.storage.FotoProdutoLocalStorageAdapter;
import com.algafoods.infra.storage.FotoProdutoS3StorageAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class StorageConfig {

    private final StorageProperties storageProperties;

    public StorageConfig(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Bean
    public S3Client createClient(){
        return S3Client.builder()
                .region(Region.of(storageProperties.getS3().getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                storageProperties.getS3().getAccessKeyId(),
                                storageProperties.getS3().getAccessKeySecret()
                        )
                ))
                .build();
    }

    @Bean
    public FotoProdutoStoragePort fotoStorageService(){
        if (StorageProperties.StorageType.S3.equals(storageProperties.getStorageType())) {
            return new FotoProdutoS3StorageAdapter();
        } else {
            return new FotoProdutoLocalStorageAdapter();
        }
    }


}
