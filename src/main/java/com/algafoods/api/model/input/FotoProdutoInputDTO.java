package com.algafoods.api.model.input;

import com.algafoods.core.validation.ContentType;
import com.algafoods.core.validation.FileSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FotoProdutoInputDTO {

    @NotNull
    @FileSize(max = "10MB")
    @ContentType(allowedMediaTypes = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    private MultipartFile arquivo;

    @NotBlank
    private String descricao;

}
