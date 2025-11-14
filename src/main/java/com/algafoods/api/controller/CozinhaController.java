package com.algafoods.api.controller;

import com.algafoods.api.domain.exception.EntidadeEmUsoException;
import com.algafoods.api.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.api.domain.model.Cozinha;
import com.algafoods.api.domain.repository.CozinhaRepository;
import com.algafoods.api.domain.service.CadastroCozinhaService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @GetMapping
    public List<Cozinha> listar(){
        return cozinhaRepository.findAll();
    }

    @GetMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> buscar(
            @PathVariable
            Long cozinhaId
    ){
        Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);

        if(cozinha.isPresent()) {
            return ResponseEntity.ok(cozinha.get());
        }

        return ResponseEntity.notFound().build();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "http://localhost:8080/cozinhas");
//
//        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
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

//       HttpHeaders headers = new HttpHeaders();
//       headers.add("Location", "http://localhost:8080/cozinhas");
//
//       return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @PostMapping
    public ResponseEntity<Cozinha> adicionar(
            @RequestBody
            Cozinha cozinha
    ){
        if(cozinha == null){
            return ResponseEntity.badRequest().build();
        }
        Cozinha newCozinha = cadastroCozinhaService.salvar(cozinha);
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

        Optional<Cozinha> cozinhaAtual =  cozinhaRepository.findById(cozinhaId);

        if(cozinhaAtual.isPresent()){
            BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");

            Cozinha novaCozinha = cadastroCozinhaService.salvar(cozinhaAtual.get());

            return ResponseEntity.ok(novaCozinha);
        }

        return ResponseEntity.notFound().build();


    }

    @DeleteMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> remover(
            @PathVariable
            Long cozinhaId
    ) {
        try {
            cadastroCozinhaService.excluir(cozinhaId);
            return ResponseEntity.noContent().build();
        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
