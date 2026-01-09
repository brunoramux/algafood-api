package com.algafoods.api.controller;

import com.algafoods.api.mappers.CidadeMapper;
import com.algafoods.api.model.CidadeModel;
import com.algafoods.api.model.input.cidades.CidadeInputDTO;
import com.algafoods.api.utils.ResourceUriHelper;
import com.algafoods.domain.service.CidadeService;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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

    @GetMapping("/{id}")
    public CidadeModel buscar(
            @PathVariable
            Long id
    ){
        var cidade = cidadeMapper.toModel(cidadeService.findById(id));

        cidade.add(
                linkTo(methodOn(CidadeController.class).buscar(id))
                        .withSelfRel()
        );

        cidade.add(
                linkTo(methodOn(CidadeController.class).listar())
                        .withRel("cidades")
        );

        cidade.getEstado().add(
                linkTo(methodOn(EstadoController.class).buscar(cidade.getEstado().getId()))
                        .withRel("estado")
        );

        return cidade;
    }

    @PostMapping
    public CidadeModel cadastrar(
            @RequestBody CidadeInputDTO cidadeInputDTO
    ) {

        var cidade = cidadeService.create(cidadeMapper.toDomain(cidadeInputDTO));

        // Adiciona o header Location na resposta com a URI da nova cidade criada seguindo padroes REST HATEOAS
        ResourceUriHelper.addUriInResponseHeader(cidade.getId());

        return cidadeMapper.toModel(cidade);
    }
}
