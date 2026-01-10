package com.algafoods.api.controller;

import com.algafoods.api.mappers.CidadeMapper;
import com.algafoods.api.model.CidadeModel;
import com.algafoods.api.model.input.cidades.CidadeInputDTO;
import com.algafoods.api.utils.ResourceUriHelper;
import com.algafoods.domain.model.Cidade;
import com.algafoods.domain.service.CidadeService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    public CollectionModel<CidadeModel> listar(){

        List<Cidade> cidades = cidadeService.findAll();

        return cidadeMapper.toCollectionModel(cidades);

    }

    @GetMapping("/{id}")
    public CidadeModel buscar(
            @PathVariable
            Long id
    ){
        return cidadeMapper.toModel(cidadeService.findById(id));
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
