package com.algafoods.api.mappers;

import com.algafoods.api.model.input.PermissaoInputDTO;
import com.algafoods.api.model.output.PermissaoOutputDTO;
import com.algafoods.domain.model.Permissao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PermissaoMapper {
    private final ModelMapper mapper;

    public PermissaoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Permissao toDomain(PermissaoInputDTO permissao){
        return mapper.map(permissao, Permissao.class);
    }

    public PermissaoOutputDTO toModel(Permissao permissao) {
        return mapper.map(permissao, PermissaoOutputDTO.class);
    }
}
