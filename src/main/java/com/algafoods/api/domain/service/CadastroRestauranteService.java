package com.algafoods.api.domain.service;

import com.algafoods.api.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.api.domain.model.Cozinha;
import com.algafoods.api.domain.model.Restaurante;
import com.algafoods.api.domain.repository.CozinhaRepository;
import com.algafoods.api.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroRestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cozinha informada não foi encontrada."));

        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    }


}
