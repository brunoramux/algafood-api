package com.algafoods.api.domain.service;

import com.algafoods.api.domain.model.FormaPagamento;
import com.algafoods.api.domain.repository.FormaPagamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class CadastroFormaPagamentoService {
    private final FormaPagamentoRepository formaPagamentoRepository;

    public CadastroFormaPagamentoService(FormaPagamentoRepository formaPagamentoRepository) {
        this.formaPagamentoRepository = formaPagamentoRepository;
    }

    public FormaPagamento salvar(FormaPagamento formaPagamento) {
        return formaPagamentoRepository.save(formaPagamento);
    }
}
