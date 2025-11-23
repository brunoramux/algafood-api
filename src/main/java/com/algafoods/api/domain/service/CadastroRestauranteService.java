package com.algafoods.api.domain.service;

import com.algafoods.api.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.api.domain.model.Cozinha;
import com.algafoods.api.domain.model.FormaPagamento;
import com.algafoods.api.domain.model.Restaurante;
import com.algafoods.api.domain.repository.CozinhaRepository;
import com.algafoods.api.domain.repository.FormaPagamentoRepository;
import com.algafoods.api.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CadastroRestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    public Restaurante create(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cozinha informada não foi encontrada."));

        restaurante.setCozinha(cozinha);

        Set<FormaPagamento> formasPagamento = restaurante.getFormasPagamento()
                .stream()
                .map(fp -> formaPagamentoRepository.findById(fp.getId())
                        .orElseThrow(() -> new EntidadeNaoEncontradaException(
                                "Forma de Pagamento informada não foi encontrada: " + fp.getId())))
                .collect(Collectors.toSet());

        restaurante.setFormasPagamento(formasPagamento.stream().toList());
        return restauranteRepository.save(restaurante);
    }

    public Restaurante update(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cozinha informada não foi encontrada."));

        restaurante.setCozinha(cozinha);

        return restauranteRepository.save(restaurante);
    }


}
