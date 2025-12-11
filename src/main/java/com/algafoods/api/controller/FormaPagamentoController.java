package com.algafoods.api.controller;

import com.algafoods.domain.exception.EntidadeEmUsoException;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.FormaPagamento;
import com.algafoods.domain.repository.FormaPagamentoRepository;
import com.algafoods.domain.service.FormaPagamentoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    private final FormaPagamentoService formaPagamentoService;

    public FormaPagamentoController(FormaPagamentoService formaPagamentoService) {
        this.formaPagamentoService = formaPagamentoService;
    }

    @GetMapping
    public List<FormaPagamento> listar(){
        return formaPagamentoService.getAllFormaPagamentos();
    }

    @GetMapping("/{id}")
    public FormaPagamento buscar(@PathVariable Long id){
        return formaPagamentoService.getFormaPagamentoById(id);
    }

    @PostMapping
    public ResponseEntity<?> adicionar(
            @RequestBody
            FormaPagamento formaPagamento
    ){
        try {
            FormaPagamento newFormaPagamento =  formaPagamentoService.save(formaPagamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(newFormaPagamento);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(String.format("{ 'message': '%s' }",  e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        try {
            formaPagamentoService.deleteFormaPagamentoById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException("Forma de Pagamento não pode ser excluída pois estáo em uso.");
        }

    }
}
