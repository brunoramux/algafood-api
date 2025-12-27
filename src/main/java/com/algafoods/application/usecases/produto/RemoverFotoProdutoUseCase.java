package com.algafoods.application.usecases.produto;

import com.algafoods.application.port.FotoProdutoPort;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.FotoProduto;
import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.repository.ProdutoRepository;
import com.algafoods.domain.repository.RestauranteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoverFotoProdutoUseCase {
    private final FotoProdutoPort repository;
    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;

    public RemoverFotoProdutoUseCase(FotoProdutoPort repository, ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    @Transactional
    public void remove(Long restauranteId, Long produtoId){
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Restaurante não encontrado.")
        );

        Produto produto = produtoRepository.findByIdAndRestaurante(produtoId, restaurante).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Produto não encontrado.")
        );

        FotoProduto fotoProduto = repository.findById(produto.getId()).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Foto do produto não encontrada.")
        );

        repository.delete(fotoProduto);
    }
}
