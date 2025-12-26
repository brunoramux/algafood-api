package com.algafoods.api.controller;

import com.algafoods.api.model.input.FotoProdutoInputDTO;
import com.algafoods.application.usecases.produto.CadastroFotoProdutoUseCase;
import com.algafoods.domain.model.FotoProduto;
import com.algafoods.domain.model.Produto;
import com.algafoods.domain.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/restaurantes/{restauranteId}")
public class UploadController {

    private final ProdutoService produtoService;
    private final CadastroFotoProdutoUseCase cadastroFotoProdutoUseCase;

    public UploadController(ProdutoService produtoService, CadastroFotoProdutoUseCase cadastroFotoProdutoUseCase) {
        this.produtoService = produtoService;
        this.cadastroFotoProdutoUseCase = cadastroFotoProdutoUseCase;
    }

    @PostMapping(value = "/produtos/{produtoId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadProdutoFoto(
            @PathVariable Long produtoId,
            @PathVariable Long restauranteId,
            @Valid
            FotoProdutoInputDTO fotoProdutoInputDTO
    ){
        Produto produto = produtoService.find(restauranteId, produtoId);

        var nomeArquivo = UUID.randomUUID().toString() + "_" + fotoProdutoInputDTO.getArquivo().getOriginalFilename();
        var arquivoFoto = Path.of("/Users/brunoramoslemos/fotos_teste", nomeArquivo);
        var tipoArquivo = fotoProdutoInputDTO.getArquivo().getContentType();
        var tamanhoArquivo = fotoProdutoInputDTO.getArquivo().getSize();

        FotoProduto fotoProduto = new FotoProduto();
        fotoProduto.setProduto(produto);
        fotoProduto.setDescricao(fotoProdutoInputDTO.getDescricao());
        fotoProduto.setTamanho(tamanhoArquivo);
        fotoProduto.setContentType(tipoArquivo);
        fotoProduto.setNomeArquivo(nomeArquivo);

        try {
            fotoProdutoInputDTO.getArquivo().transferTo(arquivoFoto);
            cadastroFotoProdutoUseCase.save(fotoProduto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
