package com.algafoods.api.mappers;

import com.algafoods.api.controller.CidadeController;
import com.algafoods.api.controller.UsuarioController;
import com.algafoods.api.model.input.usuarios.UsuarioInputDTO;
import com.algafoods.api.model.output.usuarios.UsuarioOutputDTO;
import com.algafoods.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioMapper extends RepresentationModelAssemblerSupport<Usuario, UsuarioOutputDTO> {

    private final ModelMapper mapper;

    public UsuarioMapper(ModelMapper mapper) {

        super(UsuarioController.class, UsuarioOutputDTO.class);
        this.mapper = mapper;

    }

    public UsuarioOutputDTO toModel(Usuario usuario) {

        UsuarioOutputDTO usuarioOutputDTO = mapper.map(usuario, UsuarioOutputDTO.class);

        usuarioOutputDTO.add(
                linkTo(methodOn(UsuarioController.class).listar())
                .withSelfRel()
        );

        return usuarioOutputDTO;

    }

    @Override
    public CollectionModel<UsuarioOutputDTO> toCollectionModel(Iterable<? extends Usuario> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(UsuarioController.class).listar()).withSelfRel());
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
