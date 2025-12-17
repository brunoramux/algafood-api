package com.algafoods.api.controller;

import com.algafoods.api.mappers.GrupoMapper;
import com.algafoods.api.mappers.PermissaoMapper;
import com.algafoods.api.model.GrupoModel;
import com.algafoods.api.model.output.PermissaoOutputDTO;
import com.algafoods.domain.service.GrupoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    private final GrupoService grupoService;
    private final GrupoMapper grupoMapper;
    private final PermissaoMapper permissaoMapper;

    public GrupoController(GrupoService grupoService, GrupoMapper grupoMapper, PermissaoMapper permissaoMapper) {
        this.grupoService = grupoService;
        this.grupoMapper = grupoMapper;
        this.permissaoMapper = permissaoMapper;
    }

    @GetMapping
    public List<GrupoModel> listar(){
        return grupoService.findAll().stream()
                .map(grupoMapper::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/permissoes")
    public Set<PermissaoOutputDTO> listarPermissoes(
            @PathVariable Long id
    ){
        return grupoService.listarPermissoes(id).stream()
                .map(permissaoMapper::toModel)
                .collect(Collectors.toSet());
    }

    @PutMapping("/{grupoId}/permissoes/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associarPermissao(
            @PathVariable
            Long grupoId,
            @PathVariable
            Long permissaoId
    ){
        grupoService.associarPermissao(grupoId, permissaoId);
    }

    @DeleteMapping("/{grupoId}/permissoes/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociarPermissao(
            @PathVariable
            Long grupoId,
            @PathVariable
            Long permissaoId
    ){
        grupoService.desassociarPermissao(grupoId, permissaoId);
    }
}
