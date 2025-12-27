package com.algafoods.api.controller;

import com.algafoods.api.mappers.FotoProdutoMapper;
import com.algafoods.api.mappers.ProdutoMapper;
import com.algafoods.api.mappers.RestauranteMapper;
import com.algafoods.api.mappers.UsuarioMapper;
import com.algafoods.api.model.input.IncluirFormaPagamentoEmRestauranteDTO;
import com.algafoods.api.model.input.ProdutoInputDTO;
import com.algafoods.api.model.input.RestauranteInputDTO;
import com.algafoods.api.model.output.FotoProdutoOutputDTO;
import com.algafoods.api.model.output.RestauranteOutputDTO;
import com.algafoods.api.model.output.UsuarioOutputDTO;
import com.algafoods.api.model.output.UsuarioResponsavelRestauranteOutputDTO;
import com.algafoods.application.usecases.produto.BuscarFotoProdutoUseCase;
import com.algafoods.core.validation.ValidacaoException;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.FormaPagamento;
import com.algafoods.domain.model.FotoProduto;
import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.repository.RestauranteRepository;
import com.algafoods.domain.service.FormaPagamentoService;
import com.algafoods.domain.service.ProdutoService;
import com.algafoods.domain.service.RestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;
    private final FormaPagamentoService formaPagamentoService;
    private final ProdutoService produtoService;

    private final RestauranteMapper restauranteMapper;
    private final ProdutoMapper produtoMapper;
    private final FotoProdutoMapper fotoProdutoMapper;

    private final SmartValidator validator;
    private final UsuarioMapper usuarioMapper;

    private final BuscarFotoProdutoUseCase buscarFotoProdutoUseCase;

    public RestauranteController(
            RestauranteRepository restauranteRepository,
            RestauranteService restauranteService,
            FormaPagamentoService formaPagamentoService,
            ProdutoService produtoService, FotoProdutoMapper fotoProdutoMapper,
            SmartValidator validator,
            RestauranteMapper restauranteMapper,
            ProdutoMapper produtoMapper,
            UsuarioMapper usuarioMapper, BuscarFotoProdutoUseCase buscarFotoProdutoUseCase) {
        this.restauranteService = restauranteService;
        this.fotoProdutoMapper = fotoProdutoMapper;
        this.validator = validator;
        this.restauranteMapper = restauranteMapper;
        this.formaPagamentoService = formaPagamentoService;
        this.produtoMapper = produtoMapper;
        this.produtoService = produtoService;
        this.usuarioMapper = usuarioMapper;
        this.buscarFotoProdutoUseCase = buscarFotoProdutoUseCase;
    }


    // ENDPOINTS DE LISTAGEM DE RESTAURANTES

    @GetMapping
    public List<RestauranteOutputDTO> listar(){
        return  restauranteService.findAll().stream()
                .map(restauranteMapper::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<RestauranteOutputDTO> buscarPorId(
            @PathVariable
            Long restauranteId
    ){
        Restaurante restaurante = restauranteService.find(restauranteId);

        return ResponseEntity.ok(restauranteMapper.toModel(restaurante));
    }


    // ENDPOINTS DE FORMA DE PAGAMENTO VINCULADO A RESTAURANTE

    @GetMapping("/{restauranteId}/formas-pagamento")
    public ResponseEntity<List<FormaPagamento>> buscarFormasPagamento(
            @PathVariable
            Long restauranteId
    ){
        Restaurante restaurante = restauranteService.find(restauranteId);

        return ResponseEntity.ok(restaurante.getFormasPagamento());
    }

    @PutMapping("/{restauranteId}/formas-pagamento")
    public void vincularFormaPagamento(
            @PathVariable Long restauranteId,
            @RequestBody List<IncluirFormaPagamentoEmRestauranteDTO> formasPagamento
    ){
        Restaurante restaurante = restauranteService.find(restauranteId);

        formasPagamento.forEach(formaPagamento -> {
            FormaPagamento newFormaPagamento = formaPagamentoService.getFormaPagamentoById(formaPagamento.getId());
            if(!restaurante.getFormasPagamento().contains(newFormaPagamento)) {
                restaurante.getFormasPagamento().add(newFormaPagamento);
            }
        });

        restauranteService.save(restaurante);
    }


    // ENDPOINTS PARA PROCURAR RESTAURANTES

    @GetMapping("/procurar")
    public ResponseEntity<List<Restaurante>> buscarPorTaxa(
            @RequestParam
            BigDecimal taxaInicial,
            BigDecimal taxaFinal
    ){
        List<Restaurante> restaurantes = restauranteService.findByTaxaFreteBetween(taxaInicial, taxaFinal);

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
        List<Restaurante> restaurantes = restauranteService.findByNomeAndTaxaFrete(nome, taxaInicial, taxaFinal);

        if(restaurantes == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/findFreteGratis")
    public ResponseEntity<List<Restaurante>> buscarPorFreteGratis(
            @RequestParam(required = false) String nome
    ){

        var restaurantes = restauranteService.findByFreteGratis(nome);

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/findFirst")
    public ResponseEntity<Restaurante> findFirst(){

        var restaurante = restauranteService.findFirst();

        if (restaurante.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restaurante.get());
    }

    // ENDPOINTS PARA ADICIONAR E ATUALIZAR RESTAURANTE

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
            Restaurante restauranteAtual = restauranteService.find(restauranteId);

            // TRANSFORMAR O OBJETO RECEBIDO NA REQUISIÇÃO PARA EVITAR ERROS NO JPA
            restauranteMapper.copyToDomainObject(restauranteInputDTO, restauranteAtual);
            // SETAR TIMESTAMP DE ATUALIZACAO COM TIMEZONE SEMPRE EM UTC
            restauranteAtual.setDataAtualizacao(OffsetDateTime.now());
            // ATUALIZA NO BANCO DE DADOS
            Restaurante restauranteAtualizado = restauranteService.update(restauranteAtual);
            // RETORNA OBJETO DTO A PARTIR DO OBJETO ATUALZIADO
            return ResponseEntity.ok(restauranteMapper.toModel(restauranteAtualizado));
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
        Restaurante restauranteAtual = restauranteService.find(restauranteId);

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


    // ENDPOINTS DE ATIVAÇÃO DE RESTAURANTE

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

    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(
            @RequestBody
            List<Long> restauranteIds
    ){
        restauranteService.ativar(restauranteIds);
    }

    @PutMapping("/desativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarMultiplos(
            @RequestBody
            List<Long> restauranteIds
    ){
        restauranteService.desativar(restauranteIds);
    }


    // ENDPOINTS DE ABERTURA E FECHAMENTO DE RESTAURANTE

    @PutMapping("/{restauranteId}/abrir")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void abrir(
            @PathVariable
            Long restauranteId
    ) {
        restauranteService.abrir(restauranteId);
    }

    @PutMapping("/{restauranteId}/fechar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fechar(
            @PathVariable
            Long restauranteId
    ) {
        restauranteService.fechar(restauranteId);
    }


    // ENDPOINT PARA REMOVER UM RESTAURANTE

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @PathVariable
            Long id
    ){
        restauranteService.delete(id);
    }



    // ENDPOINTS PARA PRODUTOS VINCULADOS A RESTAURANTES

    @GetMapping("/{id}/produtos")
    public List<Produto> listar(
            @PathVariable
            Long id,
            @RequestParam(required = false) boolean ativo
    ){
        if(ativo){
            return produtoService.findAtivosByRestaurante(id);
        }
        return produtoService.list(id);
    }

    @PostMapping("/{id}/produtos")
    public Produto cadastrarProduto(
            @PathVariable
            Long id,
            @RequestBody
            ProdutoInputDTO produtoInputDTO
    ){
        Restaurante restaurante = restauranteService.find(id);

        Produto produto = produtoMapper.toDomain(produtoInputDTO);
        produto.setRestaurante(restaurante);

        return produtoService.create(produto);
    }

    @GetMapping("/{restauranteId}/produtos/{produtoId}/fotos")
    public FotoProdutoOutputDTO buscarFotoProduto(
            @PathVariable Long restauranteId,
            @PathVariable Long produtoId
    ){
        FotoProduto fotoProduto = buscarFotoProdutoUseCase.execute(restauranteId, produtoId).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Foto do produto não encontrada.")
        );

        return fotoProdutoMapper.toModel(fotoProduto);
    }


    // ENDPOINTS PARA VINCULAR, DESVINCULAR E MOSTRAR USUÁRIO RESPONSÁVEL PELO RESTAURANTE

    @PutMapping("/{restauranteId}/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vincularUsuario(
            @PathVariable
            Long restauranteId,
            @PathVariable
            Long usuarioId
    ){
        restauranteService.vincularUsuarioResponsavel(restauranteId, usuarioId);
    }

    @DeleteMapping("/{restauranteId}/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desvincularUsuario(
            @PathVariable
            Long restauranteId,
            @PathVariable
            Long usuarioId
    ){
        restauranteService.desvincularUsuarioResponsavel(restauranteId, usuarioId);
    }

    @GetMapping("/{id}/responsaveis")
    public Set<UsuarioOutputDTO> listarUsuariosResponsaveis(
            @PathVariable
            Long id
    ){
        return restauranteService.findUsuariosResponsaveis(id).stream()
                .map(usuarioMapper::toModel)
                .collect(Collectors.toSet());
    }

    private void validate(Restaurante restaurante, String objectName){
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
        validator.validate(restaurante, bindingResult);

        if(bindingResult.hasErrors()){
            throw new ValidacaoException(bindingResult);
        }
    }
}

