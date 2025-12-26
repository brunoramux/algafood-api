package com.algafoods.api.controller;

import com.algafoods.api.model.input.FotoProdutoInputDTO;
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

    public UploadController(ProdutoService produtoService) {
        this.produtoService = produtoService;
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

        System.out.println(tamanhoArquivo);
        try {
            fotoProdutoInputDTO.getArquivo().transferTo(arquivoFoto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
