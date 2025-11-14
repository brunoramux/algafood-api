package com.algafoods.api.domain.repository;

import com.algafoods.api.domain.model.Estado;

import java.util.List;

public interface EstadoRepository {
    List<Estado> listar();
    Estado buscarPorId(Long id);
    Estado salvar(Estado estado);
    void remover(Estado estado);
}
