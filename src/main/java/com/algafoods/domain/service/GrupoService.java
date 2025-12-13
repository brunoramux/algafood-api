package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeEmUsoException;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Grupo;
import com.algafoods.domain.repository.GrupoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GrupoService {
    public static final String MENSAGEM_GRUPO_NAO_ENCONTRADO = "Grupo com o código %d não encontrado.";
    public static final String MENSAGEM_GRUPO_EM_USO = "Grupo em uso.";

    private final GrupoRepository repository;

    public GrupoService(GrupoRepository repository) {
        this.repository = repository;
    }

    public List<Grupo> findAll(){
        return repository.findAll();
    }

    public Grupo find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_GRUPO_NAO_ENCONTRADO,  id)));
    }

    @Transactional
    public Grupo create(Grupo grupo) {
        return repository.save(grupo);
    }

    @Transactional
    public void delete(Long id) {
        Grupo grupo = this.find(id);

        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(MENSAGEM_GRUPO_EM_USO);
        }

    }

    @Transactional
    public Grupo update(Grupo grupo) {
        return repository.save(grupo);
    }
}
