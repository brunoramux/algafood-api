package com.algafoods.api.mappers;

import com.algafoods.api.model.CozinhaModel;
import com.algafoods.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CozinhaMapper {

    private final ModelMapper mapper;

    public CozinhaMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CozinhaModel toModel(Cozinha cozinha) {
        return mapper.map(cozinha, CozinhaModel.class);
    }
}
