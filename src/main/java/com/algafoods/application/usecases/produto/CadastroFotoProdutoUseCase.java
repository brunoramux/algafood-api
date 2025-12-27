package com.algafoods.application.usecases.produto;

import com.algafoods.application.port.FotoProdutoPort;
import com.algafoods.domain.model.FotoProduto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroFotoProdutoUseCase {

    private final FotoProdutoPort repository;
    private final BuscarFotoProdutoUseCase buscarFotoProdutoUseCase;
    private final RemoverFotoProdutoUseCase removerFotoProdutoUseCase;

    public CadastroFotoProdutoUseCase(FotoProdutoPort repository, BuscarFotoProdutoUseCase buscarFotoProdutoUseCase, RemoverFotoProdutoUseCase removerFotoProdutoUseCase) {
        this.repository = repository;
        this.buscarFotoProdutoUseCase = buscarFotoProdutoUseCase;
        this.removerFotoProdutoUseCase = removerFotoProdutoUseCase;
    }

    public FotoProduto execute(FotoProduto fotoProduto) {
        Optional<FotoProduto> fotoProdutoExistente = buscarFotoProdutoUseCase.execute(fotoProduto.getProduto().getRestaurante().getId(), fotoProduto.getProduto().getId());

        if (fotoProdutoExistente.isPresent()) {
            removerFotoProdutoUseCase.execute(fotoProdutoExistente.get().getProduto().getRestaurante().getId(), fotoProdutoExistente.get().getProduto().getId());
        }

        return this.repository.save(fotoProduto);
    }
}
