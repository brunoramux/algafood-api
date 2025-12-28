package com.algafoods.core.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

// CLASSE QUE MAPEIA PROPRIEDADES DO ARQUIVO APPLICATION.PROPERTIES PARA OBJETOS JAVA
@Getter
@Setter
@Component
// CONFIGURAÇÃO PARA QUE SEJA FEITA LEITURA AUTOMATICA DAS PROPRIEDADES NO ARQUIVO APPLICATION.PROPERTIES. OS NOMES DEVEM BATER
@ConfigurationProperties("algafood.storage")
public class StorageProperties {

    private Local local = new Local();
    private S3 s3 = new S3();
    private StorageType storageType;

    public enum StorageType {
        LOCAL, S3
    }

    @Getter
    @Setter
    public class Local {
        private Path diretorioFotos;
    }

    @Getter
    @Setter
    public class S3 {

        private String accessKeyId;
        private String accessKeySecret;
        private String bucketName;
        private String region;
        private String fotosDir;

    }

}
