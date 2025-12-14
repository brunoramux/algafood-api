package com.algafoods.api.mappers;

import com.algafoods.api.model.input.ProdutoInputDTO;
import com.algafoods.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    private final ModelMapper mapper;

    public ProdutoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Produto toDomain(ProdutoInputDTO produtoInputDTO) {
        return mapper.map(produtoInputDTO, Produto.class);
    }
}
