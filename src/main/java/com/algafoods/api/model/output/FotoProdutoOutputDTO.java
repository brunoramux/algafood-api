package com.algafoods.api.model.output;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoProdutoOutputDTO {
    private String nomeArquivo;
    private String descricao;
    private String contentType;
    private Long tamanho;
}
