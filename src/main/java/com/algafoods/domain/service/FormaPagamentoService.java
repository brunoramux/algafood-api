package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.FormaPagamento;
import com.algafoods.domain.repository.FormaPagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormaPagamentoService {
    public static final String MENSAGEM_FORMAPAGAMENTO_NAO_ENCONTRADA = "Forma de pagamento com o código %d não encontrada.";
    private final FormaPagamentoRepository repository;

    public FormaPagamentoService(FormaPagamentoRepository repository) {
        this.repository = repository;
    }

    public List<FormaPagamento> getAllFormaPagamentos() {
        return repository.findAll();
    }

    public FormaPagamento getFormaPagamentoById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_FORMAPAGAMENTO_NAO_ENCONTRADA, id)
                )
        );
    }

    @Transactional
    public FormaPagamento save(FormaPagamento formaPagamento) {
        return repository.save(formaPagamento);
    }

    @Transactional
    public void deleteFormaPagamentoById(Long id) {
        FormaPagamento formaPagamento = getFormaPagamentoById(id);
        repository.deleteById(id);
    }
}
