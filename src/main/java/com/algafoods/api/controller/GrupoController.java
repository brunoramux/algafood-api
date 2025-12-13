package com.algafoods.api.controller;

import com.algafoods.api.mappers.GrupoMapper;
import com.algafoods.api.model.GrupoModel;
import com.algafoods.domain.service.GrupoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    private final GrupoService grupoService;
    private final GrupoMapper grupoMapper;

    public GrupoController(GrupoService grupoService, GrupoMapper grupoMapper) {
        this.grupoService = grupoService;
        this.grupoMapper = grupoMapper;
    }

    @GetMapping
    public List<GrupoModel> listar(){
        return grupoService.findAll().stream()
                .map(grupoMapper::toModel)
                .collect(Collectors.toList());
    }

}
