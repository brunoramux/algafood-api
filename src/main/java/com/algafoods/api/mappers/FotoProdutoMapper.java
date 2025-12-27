package com.algafoods.api.mappers;

import com.algafoods.api.model.output.FotoProdutoOutputDTO;
import com.algafoods.domain.model.FotoProduto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FotoProdutoMapper {
    private final ModelMapper mapper;

    public FotoProdutoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public FotoProdutoOutputDTO toModel(FotoProduto fotoProduto) {
        return mapper.map(fotoProduto, FotoProdutoOutputDTO.class);
    }

}
