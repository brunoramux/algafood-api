package com.algafoods.api.controller;

import com.algafoods.api.mappers.FotoProdutoMapper;
import com.algafoods.api.mappers.ProdutoMapper;
import com.algafoods.api.model.input.ProdutoInputDTO;
import com.algafoods.api.model.output.FotoProdutoOutputDTO;
import com.algafoods.application.port.FotoProdutoStoragePort;
import com.algafoods.application.usecases.produto.BuscarFotoProdutoUseCase;
import com.algafoods.application.usecases.produto.RemoverArmazenamentoFotoProdutoUseCase;
import com.algafoods.application.usecases.produto.RemoverFotoProdutoUseCase;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.FotoProduto;
import com.algafoods.domain.model.Produto;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.service.ProdutoService;
import com.algafoods.domain.service.RestauranteService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/restaurantes/{id}/produtos")
public class ProdutoController {

    private final RestauranteService restauranteService;
    private final ProdutoService produtoService;
    private final ProdutoMapper produtoMapper;

    private final BuscarFotoProdutoUseCase buscarFotoProdutoUseCase;
    private final FotoProdutoStoragePort fotoProdutoStoragePort;
    private final FotoProdutoMapper fotoProdutoMapper;
    private final RemoverFotoProdutoUseCase removerFotoProdutoUseCase;
    private final RemoverArmazenamentoFotoProdutoUseCase removerArmazenamentoFotoProdutoUseCase;

    public ProdutoController(RestauranteService restauranteService, ProdutoService produtoService, ProdutoMapper produtoMapper, BuscarFotoProdutoUseCase buscarFotoProdutoUseCase, FotoProdutoStoragePort fotoProdutoStoragePort, FotoProdutoMapper fotoProdutoMapper, RemoverFotoProdutoUseCase removerFotoProdutoUseCase, RemoverArmazenamentoFotoProdutoUseCase removerArmazenamentoFotoProdutoUseCase) {
        this.restauranteService = restauranteService;
        this.produtoService = produtoService;
        this.produtoMapper = produtoMapper;
        this.buscarFotoProdutoUseCase = buscarFotoProdutoUseCase;
        this.fotoProdutoStoragePort = fotoProdutoStoragePort;
        this.fotoProdutoMapper = fotoProdutoMapper;
        this.removerFotoProdutoUseCase = removerFotoProdutoUseCase;
        this.removerArmazenamentoFotoProdutoUseCase = removerArmazenamentoFotoProdutoUseCase;
    }

    @GetMapping
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

    @PostMapping
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

    @GetMapping(value = "/{produtoId}/fotos", produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoProdutoOutputDTO buscarFotoProduto(
            @PathVariable Long id,
            @PathVariable Long produtoId
    ){
        FotoProduto fotoProduto = buscarFotoProdutoUseCase.execute(id, produtoId).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Foto do produto não encontrada.")
        );

        return fotoProdutoMapper.toModel(fotoProduto);
    }

    @GetMapping(value = "/{produtoId}/fotos")
    public ResponseEntity<InputStreamResource> servirFotoProduto(
            @PathVariable Long id,
            @PathVariable Long produtoId,
            @RequestHeader(name = "accept") String accept
    ) throws HttpMediaTypeNotAcceptableException {
        try {
            FotoProduto fotoProduto = buscarFotoProdutoUseCase.execute(id, produtoId).orElseThrow(
                    () -> new EntidadeNaoEncontradaException("Foto do produto não encontrada.")
            );

            MediaType mediaType = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> acceptableMediaTypes = MediaType.parseMediaTypes(accept);

            if(verificarCompatibilidadeMediaType(mediaType, acceptableMediaTypes)){
                InputStream inputStream = fotoProdutoStoragePort.recuperarFotoProduto(fotoProduto.getNomeArquivo());

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(new InputStreamResource(inputStream));
            };

            throw new HttpMediaTypeNotAcceptableException(acceptableMediaTypes);


        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{produtoId}/fotos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerFotoProduto(
            @PathVariable Long id,
            @PathVariable Long produtoId
    ){
        FotoProduto fotoProduto = buscarFotoProdutoUseCase.execute(id, produtoId).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Foto do produto não encontrada.")
        );

        removerFotoProdutoUseCase.execute(id, fotoProduto.getId());
        removerArmazenamentoFotoProdutoUseCase.execute(fotoProduto.getNomeArquivo());
    }

    private boolean verificarCompatibilidadeMediaType(MediaType mediaType, List<MediaType> acceptableMediaTypes) {
        return acceptableMediaTypes.stream().anyMatch(mediaType::isCompatibleWith);
    }
}
