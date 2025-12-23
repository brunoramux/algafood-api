package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {
    public static final String MENSAGEM_PRODUTO_NAO_ENCONTRADO= "Produto com o código %d não encontrado.";
    private final ProdutoRepository repository;
    private final RestauranteService restauranteService;


    public ProdutoService(ProdutoRepository repository, RestauranteService restauranteService) {
        this.repository = repository;
        this.restauranteService = restauranteService;
    }

    public Produto find(Long idRestaurante, Long idProduto) {

        Restaurante restaurante = restauranteService.find(idRestaurante);

        return repository.findByIdAndRestaurante(idProduto, restaurante).orElseThrow(
                () -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_PRODUTO_NAO_ENCONTRADO, idProduto)
                )
        );
//        return repository.findByRestaurante(restaurante)
//                .stream()
//                .filter(produto -> produto.getId().equals(idProduto))
//                .findFirst()
//                .orElseThrow(() ->
//                        new EntidadeNaoEncontradaException(
//                                String.format(MENSAGEM_PRODUTO_NAO_ENCONTRADO, idProduto)
//                        )
//                );
    }

    public List<Produto> findAtivosByRestaurante(Long restauranteId) {
        Restaurante restaurante = restauranteService.find(restauranteId);

        return repository.findAtivosByRestaurante(restaurante);
    }

    public List<Produto> list(Long restauranteId) {
        Restaurante restaurante = restauranteService.find(restauranteId);
        return repository.findByRestaurante(restaurante);
    }

    @Transactional
    public Produto create(Produto produto){
        return repository.save(produto);
    }
}
