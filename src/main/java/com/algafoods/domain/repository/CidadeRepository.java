package com.algafoods.domain.repository;

import com.algafoods.domain.model.Cidade;

import java.util.List;

public interface CidadeRepository {
    List<Cidade> listar();
    Cidade buscarPorNome(String nome);
    Cidade buscarPorId(Long id);
    Cidade salvar(Cidade cidade);
    void remover(Cidade cidade);
}
