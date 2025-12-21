package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Cidade;
import com.algafoods.domain.repository.CidadeRepository;
import org.springframework.stereotype.Service;

@Service
public class CidadeService {
    public static final String MENSAGEM_CIDADE_NAO_ENCONTRADA = "Cidade com o código %d não encontrada.";
    private final CidadeRepository cidadeRepository;

    public CidadeService(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    public Cidade find(Long id){
        return this.cidadeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_CIDADE_NAO_ENCONTRADA, id)
                ));
    }
}
