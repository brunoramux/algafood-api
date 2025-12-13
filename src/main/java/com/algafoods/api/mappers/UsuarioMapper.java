package com.algafoods.api.mappers;

import com.algafoods.api.model.input.UsuarioInputDTO;
import com.algafoods.api.model.output.UsuarioOutputDTO;
import com.algafoods.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    private final ModelMapper mapper;

    public UsuarioMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UsuarioOutputDTO toModel(Usuario usuario) {
        return mapper.map(usuario, UsuarioOutputDTO.class);
    }

    public Usuario toDomain(UsuarioInputDTO usuarioInputDTO) {
        return mapper.map(usuarioInputDTO, Usuario.class);
    }

    public void copyToDomainObject(UsuarioInputDTO usuarioInputDTO, Usuario usuario) {
        mapper.map(usuarioInputDTO, usuario);
    }

    public void copyToModelInputObject(Usuario usuario, UsuarioInputDTO usuarioInputDTO) {
        mapper.map(usuario, usuarioInputDTO);
    }
}
