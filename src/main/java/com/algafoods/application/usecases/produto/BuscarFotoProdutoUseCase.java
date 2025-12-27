package com.algafoods.application.usecases.produto;

import com.algafoods.application.port.FotoProdutoPort;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.FotoProduto;
import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.repository.ProdutoRepository;
import com.algafoods.domain.repository.RestauranteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarFotoProdutoUseCase {

    private final FotoProdutoPort repository;
    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;

    public BuscarFotoProdutoUseCase(FotoProdutoPort repository, ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public Optional<FotoProduto> execute(Long restauranteId, Long produtoId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Restaurante não encontrado.")
        );

        Produto produto = produtoRepository.findByIdAndRestaurante(produtoId, restaurante).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Produto não encontrado.")
        );

        return repository.findById(produto.getId());

    }
}
