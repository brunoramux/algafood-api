package com.algafoods.api.domain.service;

import com.algafoods.api.domain.exception.EntidadeEmUsoException;
import com.algafoods.api.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.api.domain.model.Cozinha;
import com.algafoods.api.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroCozinhaService {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.save(cozinha);
    }

    public void excluir(Long idCozinha){
        try {
            Optional<Cozinha> cozinha = cozinhaRepository.findById(idCozinha);
            if (cozinha.isEmpty()){
                throw new EntidadeNaoEncontradaException("Cozinha não encontrada");
            }
            cozinhaRepository.deleteById(idCozinha);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("Cozinha de código %d não pode ser removida pois está em uso.",  idCozinha)
            );
        }
    }
}
