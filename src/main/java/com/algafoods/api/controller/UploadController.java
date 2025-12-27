package com.algafoods.api.controller;

import com.algafoods.api.mappers.FotoProdutoMapper;
import com.algafoods.api.model.input.FotoProdutoInputDTO;
import com.algafoods.api.model.output.FotoProdutoOutputDTO;
import com.algafoods.application.port.FotoProdutoStoragePort;
import com.algafoods.application.usecases.produto.ArmazenarFotoProdutoUseCase;
import com.algafoods.application.usecases.produto.BuscarFotoProdutoUseCase;
import com.algafoods.application.usecases.produto.CadastroFotoProdutoUseCase;
import com.algafoods.domain.model.FotoProduto;
import com.algafoods.domain.model.Produto;
import com.algafoods.domain.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/restaurantes/{restauranteId}")
public class UploadController {

    private final ProdutoService produtoService;
    private final CadastroFotoProdutoUseCase cadastroFotoProdutoUseCase;
    private final ArmazenarFotoProdutoUseCase armazenarFotoProdutoUseCase;
    private final FotoProdutoMapper fotoProdutoMapper;

    public UploadController(ProdutoService produtoService, CadastroFotoProdutoUseCase cadastroFotoProdutoUseCase, ArmazenarFotoProdutoUseCase armazenarFotoProdutoUseCase, FotoProdutoMapper fotoProdutoMapper) {
        this.produtoService = produtoService;
        this.cadastroFotoProdutoUseCase = cadastroFotoProdutoUseCase;
        this.armazenarFotoProdutoUseCase = armazenarFotoProdutoUseCase;
        this.fotoProdutoMapper = fotoProdutoMapper;
    }

    @PostMapping(value = "/produtos/{produtoId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FotoProdutoOutputDTO uploadProdutoFoto(
            @PathVariable Long produtoId,
            @PathVariable Long restauranteId,
            @Valid
            FotoProdutoInputDTO fotoProdutoInputDTO
    ){
        Produto produto = produtoService.find(restauranteId, produtoId);

        var nomeArquivo = UUID.randomUUID().toString() + "_" + fotoProdutoInputDTO.getArquivo().getOriginalFilename();
        var tipoArquivo = fotoProdutoInputDTO.getArquivo().getContentType();
        var tamanhoArquivo = fotoProdutoInputDTO.getArquivo().getSize();

        FotoProduto fotoProduto = new FotoProduto();
        fotoProduto.setProduto(produto);
        fotoProduto.setDescricao(fotoProdutoInputDTO.getDescricao());
        fotoProduto.setTamanho(tamanhoArquivo);
        fotoProduto.setContentType(tipoArquivo);
        fotoProduto.setNomeArquivo(nomeArquivo);

        try {
            armazenarFotoProdutoUseCase.execute(fotoProdutoInputDTO.getArquivo().getInputStream(), nomeArquivo);
            FotoProduto newFotoProduto = cadastroFotoProdutoUseCase.execute(fotoProduto);

            return fotoProdutoMapper.toModel(newFotoProduto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
