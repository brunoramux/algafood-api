package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Cozinha;
import com.algafoods.domain.model.FormaPagamento;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.repository.CozinhaRepository;
import com.algafoods.domain.repository.FormaPagamentoRepository;
import com.algafoods.domain.repository.RestauranteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CadastroRestauranteService {

    public static final String MENSAGEM_RESTAURANTE_NAO_ENCONTRADO = "Restaurante com o código %d não encontrado.";

    private RestauranteRepository restauranteRepository;

    private CozinhaRepository cozinhaRepository;

    private FormaPagamentoRepository formaPagamentoRepository;

    public CadastroRestauranteService(RestauranteRepository restauranteRepository, CozinhaRepository cozinhaRepository, FormaPagamentoRepository formaPagamentoRepository) {
        this.restauranteRepository = restauranteRepository;
        this.cozinhaRepository = cozinhaRepository;
        this.formaPagamentoRepository = formaPagamentoRepository;
    }

    public Restaurante encontrarRestaurante(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_RESTAURANTE_NAO_ENCONTRADO,  restauranteId)));
    }

    @Transactional
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

    @Transactional
    public Restaurante update(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cozinha informada não foi encontrada."));

        restaurante.setCozinha(cozinha);

        return restauranteRepository.save(restaurante);
    }

    public void delete(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Restaurante não encontrado")
        );
        restauranteRepository.deleteById(restauranteId);
    }


}
