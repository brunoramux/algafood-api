package com.algafoods.domain.service;

import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final RestauranteService restauranteService;


    public ProdutoService(ProdutoRepository repository, RestauranteService restauranteService) {
        this.repository = repository;
        this.restauranteService = restauranteService;
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
