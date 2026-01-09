package com.algafoods.api.controller;

import com.algafoods.api.mappers.CidadeMapper;
import com.algafoods.api.model.CidadeModel;
import com.algafoods.api.model.input.cidades.CidadeInputDTO;
import com.algafoods.api.utils.ResourceUriHelper;
import com.algafoods.domain.service.CidadeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

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

        // Adiciona o header Location na resposta com a URI da nova cidade criada seguindo padroes REST HATEOAS
        ResourceUriHelper.addUriInResponseHeader(cidade.getId());

        return cidadeMapper.toModel(cidade);
    }
}
