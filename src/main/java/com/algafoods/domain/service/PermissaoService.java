package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Permissao;
import com.algafoods.domain.repository.PermissaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissaoService {

    private final PermissaoRepository repository;

    public PermissaoService(PermissaoRepository repository) {
        this.repository = repository;
    }

    public List<Permissao> list(){
        return repository.findAll();
    }

    public Permissao find(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Permissão não encontrada.")
        );
    }

    @Transactional
    public Permissao create(Permissao permissao) {
        return repository.save(permissao);
    }

    @Transactional
    public Permissao update(Permissao permissao) {
        return repository.save(permissao);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
