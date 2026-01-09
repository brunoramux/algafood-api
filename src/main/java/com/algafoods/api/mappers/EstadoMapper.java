package com.algafoods.api.mappers;

import com.algafoods.api.model.EstadoModel;
import com.algafoods.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EstadoMapper {

    private final ModelMapper mapper;

    public EstadoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public EstadoModel toModel(Estado estado){
        return mapper.map(estado, EstadoModel.class);
    }

    public Estado toDomain(EstadoModel estadoModel){
        return mapper.map(estadoModel, Estado.class);
    }
}
