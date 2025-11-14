package com.algafoods.api.domain.repository;

import com.algafoods.api.domain.model.FormaPagamento;

import java.util.List;

public interface FormaPagamentoRepository {
    List<FormaPagamento> listar();
    FormaPagamento buscarPorId(Long id);
    FormaPagamento buscarPorNome(String nome);
    FormaPagamento salvar(FormaPagamento formaPagamento);
    void remover(FormaPagamento formaPagamento);
}
