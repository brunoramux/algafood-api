package com.algafoods.api.controller;

import com.algafoods.api.mappers.PermissaoMapper;
import com.algafoods.api.model.input.PermissaoInputDTO;
import com.algafoods.api.model.output.PermissaoOutputDTO;
import com.algafoods.domain.model.Permissao;
import com.algafoods.domain.service.PermissaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {

    private final PermissaoService permissaoService;
    private final PermissaoMapper mapper;

    public PermissaoController(PermissaoService permissaoService, PermissaoMapper mapper) {
        this.permissaoService = permissaoService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<PermissaoOutputDTO> listar(){
        return permissaoService.list().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PermissaoOutputDTO find(
            @PathVariable
            Long id
    ){
        return mapper.toModel(permissaoService.find(id));
    }

    @PostMapping
    public PermissaoOutputDTO adicionar(
            @RequestBody
            PermissaoInputDTO permissao
    ) {
        Permissao newPermissao = mapper.toDomain(permissao);

        return mapper.toModel(permissaoService.create(newPermissao));
    }
}
