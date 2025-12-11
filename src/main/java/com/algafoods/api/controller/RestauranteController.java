package com.algafoods.api.controller;

import com.algafoods.api.mappers.RestauranteMapper;
import com.algafoods.api.model.input.RestauranteInputDTO;
import com.algafoods.api.model.output.RestauranteOutputDTO;
import com.algafoods.core.validation.ValidacaoException;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.FormaPagamento;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.repository.RestauranteRepository;
import com.algafoods.domain.service.RestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;

    private final RestauranteService restauranteService;

    private final RestauranteMapper restauranteMapper;

    private final SmartValidator validator;

    public RestauranteController(RestauranteRepository restauranteRepository, RestauranteService restauranteService, SmartValidator validator, RestauranteMapper restauranteMapper) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteService = restauranteService;
        this.validator = validator;
        this.restauranteMapper = restauranteMapper;
    }

    @GetMapping
    public List<RestauranteOutputDTO> listar(){
        return  restauranteRepository.findAll().stream()
                .map(restauranteMapper::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<RestauranteOutputDTO> buscarPorId(
            @PathVariable
            Long restauranteId
    ){
        Restaurante restaurante = restauranteService.encontrarRestaurante(restauranteId);

        return ResponseEntity.ok(restauranteMapper.toModel(restaurante));
    }

    @GetMapping("/{restauranteId}/formas-pagamento")
    public ResponseEntity<List<FormaPagamento>> buscarFormasPagamento(
            @PathVariable
            Long restauranteId
    ){
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante não encontrado."));

        return ResponseEntity.ok(restaurante.getFormasPagamento());
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

    @GetMapping("/find")
    public ResponseEntity<List<Restaurante>> buscarPorNomeETaxa(
            @RequestParam(required = false)
            String nome,
            @RequestParam(required = false)
            BigDecimal taxaInicial,
            @RequestParam(required = false)
            BigDecimal taxaFinal
    ){
        List<Restaurante> restaurantes = restauranteRepository.find(nome, taxaInicial, taxaFinal);

        if(restaurantes == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/findFreteGratis")
    public ResponseEntity<List<Restaurante>> buscarPorFreteGratis(
            @RequestParam(required = false) String nome
    ){

        var restaurantes = restauranteRepository.findFreteGratis(nome);

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/findFirst")
    public ResponseEntity<Restaurante> FindFirst(){

        var restaurante = restauranteRepository.buscarPrimeiro();

        if (restaurante.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restaurante.get());
    }

    @PostMapping
    public ResponseEntity<?> adicionar(
            @RequestBody
            @Valid
            RestauranteInputDTO restaurante
    ){
            Restaurante restauranteModel = restauranteMapper.toDomain(restaurante);
            RestauranteOutputDTO newRestaurante =  restauranteMapper.toModel(restauranteService.create(restauranteModel));
            return ResponseEntity.status(HttpStatus.CREATED).body(newRestaurante);
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(
            @PathVariable
            Long restauranteId,
            @RequestBody
            @Valid
            RestauranteInputDTO restauranteInputDTO
    ){
            Restaurante restauranteAtual = restauranteService.encontrarRestaurante(restauranteId);
            restauranteMapper.copyToDomainObject(restauranteInputDTO, restauranteAtual);
            restauranteAtual.setDataAtualizacao(OffsetDateTime.now());
            Restaurante restauranteAtualizado = restauranteService.update(restauranteAtual);
            return ResponseEntity.ok(restauranteAtualizado);
    }

    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(
            @PathVariable
            Long restauranteId,
            @RequestBody
            Map<String, Object> campos_atualizar,
            HttpServletRequest request
    ){
        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
        // ENCONTRA O RESTAURANTE A SER ATUALIZADO
        Restaurante restauranteAtual = restauranteService.encontrarRestaurante(restauranteId);

        try {
            // CRIA OBJETO RESTAURANTE APENAS COM DADOS PASSADOS NA REQUISIÇÃO (DADOS A SEREM ATUALIZADOS). FAZ TODAS AS CONVERSÕES NECESSÁRIAS
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            Restaurante restauranteOrigem = mapper.convertValue(campos_atualizar, Restaurante.class);

            // ITERA NOS CAMPOS QUE DEVEM SER ALTERADOS E ATRAVÉS DO OBJETO FIELD, ALTERA O CAMPO CORRETO NO NOVO OBJETO RESTAURANTE
            campos_atualizar.forEach((nomePropriedade, valorPropriedade) -> {
                Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
                if(field != null){
                    field.setAccessible(true);

                    // PEGA NOVO VALOR, SETADO NO OBJETO RESTAURANTEORIGEM
                    Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

                    // ALTERA VALOR NO OBJETO DE RESTAURANTE CONSULTADO ATRAVÉS DO ID PASSADO COMO PARÂMETRO
                    ReflectionUtils.setField(field, restauranteAtual, novoValor);
                }
            });
        } catch (IllegalArgumentException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
        }

        // FAZ VALIDAÇÃO DO OBJETO FINAL
        validate(restauranteAtual, "restaurante");
        // TRANSFORMA O OBJETO EM INPUT DTO PARA PASSAR COMO PARÂMETRO PARA A FUNÇÃO
        var restauranteInputDTO = new RestauranteInputDTO();
        restauranteMapper.copyToModelInputObject(restauranteAtual, restauranteInputDTO);

        // CHAMA O METODO PARA ATUALIZAR NO BANCO DE DADOS
        return atualizar(restauranteId, restauranteInputDTO);
    }

    @PutMapping("/{restauranteId}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(
            @PathVariable
            Long restauranteId
    ){
        restauranteService.ativar(restauranteId);
    }

    @DeleteMapping("/{restauranteId}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(
            @PathVariable
            Long restauranteId
    ){
        restauranteService.desativar(restauranteId);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @PathVariable
            Long id
    ){
        restauranteService.delete(id);
    }

    private void validate(Restaurante restaurante, String objectName){
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
        validator.validate(restaurante, bindingResult);

        if(bindingResult.hasErrors()){
            throw new ValidacaoException(bindingResult);
        }
    }
}

