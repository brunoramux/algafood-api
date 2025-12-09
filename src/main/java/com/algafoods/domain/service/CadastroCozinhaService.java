package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeEmUsoException;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Cozinha;
import com.algafoods.domain.repository.CozinhaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCozinhaService {

    public static final String MENSAGEM_COZINHA_EM_USO = "Cozinha com o código %d não pode ser removida pois está em uso por um Restaurante.";
    public static final String MENSAGEM_COZINHA_NAO_ENCONTRADA = "Cozinha com o código %d não encontrada.";

    private final CozinhaRepository repository;

    public CadastroCozinhaService(CozinhaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Cozinha salvar(Cozinha cozinha) {
        return repository.save(cozinha);
    }

    public Cozinha encontrarCozinha(Long cozinhaId) {
        return repository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_COZINHA_NAO_ENCONTRADA,  cozinhaId)));
    }

    @Transactional
    public void excluir(Long idCozinha){
        this.encontrarCozinha(idCozinha);
        try {
            repository.deleteById(idCozinha);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MENSAGEM_COZINHA_EM_USO,  idCozinha)
            );
        }
    }
}
