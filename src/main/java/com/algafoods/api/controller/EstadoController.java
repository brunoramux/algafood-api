package com.algafoods.api.controller;

import com.algafoods.api.mappers.EstadoMapper;
import com.algafoods.api.model.EstadoModel;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Estado;
import com.algafoods.domain.repository.EstadoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/estados")
public class EstadoController {

    private final EstadoRepository repository;
    private final EstadoMapper mapper;

    public EstadoController(EstadoRepository repository, EstadoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Estado> listar(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public EstadoModel buscar(
            @PathVariable Long id
    ){
        Estado estado = repository.findById(id).orElseThrow(() ->  new EntidadeNaoEncontradaException("Estado não encontrado"));
        return mapper.toModel(estado);
    }

}
