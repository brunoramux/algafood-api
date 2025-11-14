package com.algafoods.api.controller;

import com.algafoods.api.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.api.domain.model.Cozinha;
import com.algafoods.api.domain.model.Restaurante;
import com.algafoods.api.domain.repository.RestauranteRepository;
import com.algafoods.api.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;


    @GetMapping
    public List<Restaurante> listar(){
        return restauranteRepository.findAll();
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> buscarPorId(
            @PathVariable
            Long restauranteId
    ){
        Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);
        if(restaurante.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(restaurante.get());
    }

    @GetMapping("/procurar")
    public ResponseEntity<List<Restaurante>> buscarPorTaxa(
            @RequestParam
            BigDecimal taxaInicial,
            BigDecimal taxaFinal
    ){
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);

        if(restaurantes == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restaurantes);
    }

    @PostMapping
    public ResponseEntity<?> adicionar(
            @RequestBody
            Restaurante restaurante
    ){
        try {
            Restaurante newRestaurante =  cadastroRestauranteService.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRestaurante);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(String.format("{ 'message': '%s' }",  e.getMessage()));
        }
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(
            @PathVariable
            Long restauranteId,
            @RequestBody
            Restaurante restaurante
    ){
        try {
            Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);

            if(restauranteAtual.isEmpty()) return ResponseEntity.notFound().build();

            BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id");
            Restaurante restauranteAtualizado = cadastroRestauranteService.salvar(restauranteAtual.get());
            return ResponseEntity.ok(restauranteAtualizado);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(
            @PathVariable
            Long restauranteId,
            @RequestBody
            Map<String, Object> campos_atualizar
    ){
        Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
        if(restauranteAtual.isEmpty()) return ResponseEntity.notFound().build();

        // CRIA OBJETO RESTAURANTE APENAS COM DADOS PASSADOS NA REQUISIÇÃO. FAZ TODAS AS CONVERSÕES NECESSÁRIAS
        ObjectMapper mapper = new ObjectMapper();
        Restaurante restauranteOrigem = mapper.convertValue(campos_atualizar, Restaurante.class);

        // ITERA NOS CAMPOS QUE DEVEM SER ALTERADOS E ATRAVÉS DO OBJETO FIELD, ALTERA O CAMPO CORRETO NO NOVO OBJETO RESTAURANTE
        campos_atualizar.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            if(field != null){
                field.setAccessible(true);

                Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

                ReflectionUtils.setField(field, restauranteAtual.get(), novoValor);
            }
        });

        return atualizar(restauranteId, restauranteAtual.get());
    }
}
