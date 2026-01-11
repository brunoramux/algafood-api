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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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


    public CozinhaController(CozinhaRepository cozinhaRepository, CozinhaService cozinhaService, CozinhaMapper cozinhaMapper, CozinhaModelAssembler cozinhaModelAssembler) {
        this.cozinhaRepository = cozinhaRepository;
        this.cozinhaService = cozinhaService;
        this.cozinhaMapper = cozinhaMapper;
        this.cozinhaModelAssembler = cozinhaModelAssembler;
    }

    @GetMapping
    public PagedResponseModel<CozinhaModel> listar(
            @PageableDefault(size = 10) Pageable pageable
    ){

        Page<Cozinha> pageCozinhas = cozinhaService.findAll(pageable);

        List<CozinhaModel> cozinhas = pageCozinhas.getContent().stream()
                .map(cozinhaModelAssembler::toModel)
                .toList();

        PageModel pageModel = new PageModel(
                pageCozinhas.getSize(),
                pageCozinhas.getTotalElements(),
                pageCozinhas.getTotalPages(),
                pageCozinhas.getNumber()
        );

        PagedResponseModel<CozinhaModel> response =
                new PagedResponseModel<>(cozinhas, pageModel);

        response.add(linkTo(
                methodOn(CozinhaController.class)
                        .listar(pageable))
                .withSelfRel());

        return response;

    }

    @GetMapping("/{cozinhaId}")
    public Cozinha buscar(
            @PathVariable
            Long cozinhaId
    ){
        return cozinhaService.encontrarCozinha(cozinhaId);
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
