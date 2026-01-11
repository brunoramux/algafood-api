package com.algafoods.api.controller;

import com.algafoods.api.mappers.CozinhaMapper;
import com.algafoods.api.model.CozinhaModel;
import com.algafoods.api.model.CozinhaModelAssembler;
import com.algafoods.api.model.pagination.PageModel;
import com.algafoods.api.model.pagination.PagedResponseModel;
import com.algafoods.domain.model.Cozinha;
import com.algafoods.domain.repository.CozinhaRepository;
import com.algafoods.domain.service.CozinhaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    private final CozinhaRepository cozinhaRepository;

    private final CozinhaService cozinhaService;

    private final CozinhaMapper cozinhaMapper;
    private final CozinhaModelAssembler cozinhaModelAssembler;
    private final PagedResourcesAssembler<Cozinha> pagedAssembler;


    public CozinhaController(CozinhaRepository cozinhaRepository, CozinhaService cozinhaService, CozinhaMapper cozinhaMapper, CozinhaModelAssembler cozinhaModelAssembler, PagedResourcesAssembler<Cozinha> pagedAssembler) {
        this.cozinhaRepository = cozinhaRepository;
        this.cozinhaService = cozinhaService;
        this.cozinhaMapper = cozinhaMapper;
        this.cozinhaModelAssembler = cozinhaModelAssembler;
        this.pagedAssembler = pagedAssembler;
    }

    // CONTRUINDO LINKS DE ENDPOINTS PAGINADO COM PagedResourcesAssembler
    @GetMapping
    public PagedResponseModel<CozinhaModel> listar(
            @PageableDefault(size = 10) Pageable pageable
    ){

        // PEGA DADOS DO SERVIÇO DE COZINHAS
        Page<Cozinha> pageCozinhas = cozinhaService.findAll(pageable);

        // CRIA UM PAGEDMODEL COM OS ITEMS DE COZINHA USANDO O PAGEDASSEMBLER QUE VEM DO PagedResourcesAssembler
        // TAMBEM TRANSFORMA OS ITENS DE COZINHA (DOMAIN) PARA COZINHAMODEL (API MODEL) USANDO O COZINHAMODELASSEMBLER
        PagedModel<CozinhaModel> pagedModel = pagedAssembler.toModel(pageCozinhas, cozinhaModelAssembler);

        // CRIA UM PAGEMODEL PERSONALIZADO PARA RETORNAR DADOS DE PAGINAÇÃO
        PageModel pageModel = new PageModel(
               pageCozinhas.getSize(),
               pageCozinhas.getTotalElements(),
                pageCozinhas.getTotalPages(),
                pageCozinhas.getNumber()
        );

        // ADICIONA OS ITENS DE PAGEDMODEL E OS LINKS DE PAGINAÇÃO, INCLUINDO O PAGAMODEL (DADOS DE PAGINAÇÃO)
        // RETORNA UM PagedResponseModel PERSONALIZADO
        return new PagedResponseModel<>(
                pagedModel.getContent().stream().toList(),
                pageModel
        ).add(pagedModel.getLinks());

    }

    @GetMapping("/{cozinhaId}")
    public CozinhaModel buscar(
            @PathVariable
            Long cozinhaId
    ){
        return cozinhaModelAssembler.toModel(cozinhaService.encontrarCozinha(cozinhaId));
    }

    @GetMapping("/procurar")
    public ResponseEntity<List<Cozinha>> buscarPorNome(
            @RequestParam
            String nome
    ){
        List<Cozinha> cozinhas = cozinhaRepository.findByNomeContaining(nome);

        if(cozinhas == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cozinhas);

    }

    @PostMapping
    public ResponseEntity<Cozinha> adicionar(
            @RequestBody
            @Valid
            Cozinha cozinha
    ){
        if(cozinha == null){
            return ResponseEntity.badRequest().build();
        }
        Cozinha newCozinha = cozinhaService.salvar(cozinha);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCozinha);
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> atualizar(
            @PathVariable
            Long cozinhaId,
            @RequestBody
            Cozinha cozinha
    ){
        if(cozinha == null){
            return ResponseEntity.notFound().build();
        }

        Cozinha cozinhaAtual =  cozinhaService.encontrarCozinha(cozinhaId);
        BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
        Cozinha novaCozinha = cozinhaService.salvar(cozinhaAtual);

        return ResponseEntity.ok(novaCozinha);
    }


    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @PathVariable
            Long cozinhaId
    ){
        cozinhaService.excluir(cozinhaId);
    }
}
