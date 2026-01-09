package com.algafoods.api.controller.openapi;

import com.algafoods.api.exceptionhandler.ExceptionHandlerMessage;
import com.algafoods.api.model.input.produtos.ProdutoInputDTO;
import com.algafoods.api.model.output.produtos.FotoProdutoOutputDTO;
import com.algafoods.domain.model.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import java.util.List;

@Tag(name = "Produtos", description = "Gerencia produtos de restaurantes.")
public interface ProdutoControllerOpenApi {

    @Operation(summary = "Lista produtos ativos de um restaurante.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos retornada com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionHandlerMessage.class))
            )
    })
    public List<Produto> listar(
            Long id,
            boolean ativo
    );

    @Operation(summary = "Cadastra um produto de um restaurante.")
    public Produto cadastrarProduto(
            Long id,
            ProdutoInputDTO produtoInputDTO
    );

    @Operation(summary = "Busca a foto de um produto de um restaurante.")
    public FotoProdutoOutputDTO buscarFotoProduto(
            Long id,
            Long produtoId
    );

    @Operation(summary = "Retorna a foto do produto de um restaurante.")
    public ResponseEntity<InputStreamResource> servirFotoProduto(
            Long id,
            Long produtoId,
            String accept
    ) throws HttpMediaTypeNotAcceptableException;

    @Operation(summary = "Remove a foto do produto de um restaurante.")
    public void removerFotoProduto(
            Long id,
            Long produtoId
    );

}
