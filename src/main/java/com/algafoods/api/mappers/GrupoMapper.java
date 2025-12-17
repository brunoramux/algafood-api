package com.algafoods.api.mappers;

import com.algafoods.api.model.GrupoModel;
import com.algafoods.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GrupoMapper {

    private final ModelMapper mapper;

    public GrupoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public GrupoModel toModel(Grupo grupo) {
        return mapper.map(grupo, GrupoModel.class);
    }

    public Grupo toDomain(GrupoModel grupoModel) {
        return mapper.map(grupoModel, Grupo.class);
    }

    public void copyToDomainObject(GrupoModel grupoModel, Grupo model) {
        mapper.map(grupoModel, model);
    }

    public void copyToModelInputObject(Grupo model, GrupoModel grupoModel) {
        mapper.map(model, grupoModel);
    }

}
