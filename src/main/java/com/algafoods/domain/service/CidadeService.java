package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Cidade;
import com.algafoods.domain.model.Estado;
import com.algafoods.domain.repository.CidadeRepository;
import com.algafoods.domain.repository.EstadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {
    public static final String MENSAGEM_CIDADE_NAO_ENCONTRADA = "Cidade com o código %d não encontrada.";
    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;

    public CidadeService(CidadeRepository cidadeRepository, EstadoRepository estadoRepository) {
        this.cidadeRepository = cidadeRepository;
        this.estadoRepository = estadoRepository;
    }

    public Cidade find(Long id){
        return this.cidadeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_CIDADE_NAO_ENCONTRADA, id)
                ));
    }

    public List<Cidade> findAll(){
        return this.cidadeRepository.findAll();
    }

    public Cidade create(Cidade cidade) {
        Estado estado = estadoRepository.findById(cidade.getEstado().getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format("Estado com o código %d não encontrado.", cidade.getEstado().getId())
                ));

        cidade.setEstado(estado);

        return this.cidadeRepository.save(cidade);
    }
}
