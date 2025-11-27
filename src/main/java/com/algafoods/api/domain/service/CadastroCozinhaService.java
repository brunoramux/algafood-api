package com.algafoods.api.domain.service;

import com.algafoods.api.domain.exception.EntidadeEmUsoException;
import com.algafoods.api.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.api.domain.model.Cozinha;
import com.algafoods.api.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroCozinhaService {

    public static final String MENSAGEM_COZINHA_EM_USO = "Cozinha com o código %d não pode ser removida pois está em uso por um Restaurante.";
    public static final String MENSAGEM_COZINHA_NAO_ENCONTRADA = "Cozinha não encontrada.";

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.save(cozinha);
    }

    public Cozinha encontrarCozinha(Long cozinhaId) {
        return cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(MENSAGEM_COZINHA_NAO_ENCONTRADA));
    }

    public void excluir(Long idCozinha){
        try {
            Optional<Cozinha> cozinha = cozinhaRepository.findById(idCozinha);
            if(cozinha.isEmpty()) {
                throw new EntidadeNaoEncontradaException(MENSAGEM_COZINHA_NAO_ENCONTRADA);
            }
            cozinhaRepository.deleteById(idCozinha);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MENSAGEM_COZINHA_EM_USO,  idCozinha)
            );
        }
    }
}
