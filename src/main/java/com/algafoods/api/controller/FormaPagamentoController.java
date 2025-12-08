package com.algafoods.api.controller;

import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.FormaPagamento;
import com.algafoods.domain.repository.FormaPagamentoRepository;
import com.algafoods.domain.service.CadastroFormaPagamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    private final FormaPagamentoRepository formaPagamentoRepository;
    private final CadastroFormaPagamentoService cadastroFormaPagamentoService;

    public FormaPagamentoController(FormaPagamentoRepository formaPagamentoRepository,  CadastroFormaPagamentoService cadastroFormaPagamentoService) {
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.cadastroFormaPagamentoService = cadastroFormaPagamentoService;
    }

    @GetMapping
    public List<FormaPagamento> listar(){
        return formaPagamentoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(
            @RequestBody
            FormaPagamento formaPagamento
    ){
        try {
            FormaPagamento newFormaPagamento =  cadastroFormaPagamentoService.salvar(formaPagamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(newFormaPagamento);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(String.format("{ 'message': '%s' }",  e.getMessage()));
        }
    }
}
