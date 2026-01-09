package com.algafoods.api.controller;

import com.algafoods.api.mappers.CidadeMapper;
import com.algafoods.api.model.CidadeModel;
import com.algafoods.api.model.input.cidades.CidadeInputDTO;
import com.algafoods.domain.service.CidadeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    private final CidadeService cidadeService;
    private final CidadeMapper cidadeMapper;

    public CidadeController(CidadeService cidadeService, CidadeMapper cidadeMapper) {
        this.cidadeService = cidadeService;
        this.cidadeMapper = cidadeMapper;
    }

    @GetMapping
    public List<CidadeModel> listar(){
        return cidadeService.findAll().stream().map(cidadeMapper::toModel).toList();
    }

    @PostMapping
    public CidadeModel cadastrar(
            @RequestBody CidadeInputDTO cidadeInputDTO
    ) {

        var cidade = cidadeService.create(cidadeMapper.toDomain(cidadeInputDTO));

        return cidadeMapper.toModel(cidade);
    }
}
