package com.algafoods.api.mappers;

import com.algafoods.api.model.CidadeModel;
import com.algafoods.api.model.input.cidades.CidadeInputDTO;
import com.algafoods.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CidadeMapper {

    private final ModelMapper mapper;

    public CidadeMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CidadeModel toModel(Cidade cidade){
        return mapper.map(cidade, CidadeModel.class);
    }

    public Cidade toDomain(CidadeInputDTO cidadeInputDTO){
        return mapper.map(cidadeInputDTO, Cidade.class);
    }
}
