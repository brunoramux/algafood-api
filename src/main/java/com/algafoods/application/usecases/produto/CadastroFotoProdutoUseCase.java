package com.algafoods.application.usecases.produto;

import com.algafoods.application.port.FotoProdutoPort;
import com.algafoods.domain.model.FotoProduto;
import org.springframework.stereotype.Service;

@Service
public class CadastroFotoProdutoUseCase {

    private final FotoProdutoPort repository;

    public CadastroFotoProdutoUseCase(FotoProdutoPort repository) {
        this.repository = repository;
    }

    public FotoProduto save(FotoProduto fotoProduto) {
        return this.repository.save(fotoProduto);
    }
}
