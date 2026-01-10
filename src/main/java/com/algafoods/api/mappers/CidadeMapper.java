package com.algafoods.api.mappers;

import com.algafoods.api.controller.CidadeController;
import com.algafoods.api.controller.EstadoController;
import com.algafoods.api.model.CidadeModel;
import com.algafoods.api.model.input.cidades.CidadeInputDTO;
import com.algafoods.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CidadeMapper extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

    private final ModelMapper mapper;

    public CidadeMapper(ModelMapper mapper) {

        super(CidadeController.class, CidadeModel.class);
        this.mapper = mapper;

    }

    @Override
    public CidadeModel toModel(Cidade cidade){

        CidadeModel cidadeModel =  mapper.map(cidade, CidadeModel.class);

        cidadeModel.add(
                linkTo(methodOn(CidadeController.class).buscar(cidade.getId()))
                        .withSelfRel()
        );

        cidadeModel.add(
                linkTo(methodOn(CidadeController.class).listar())
                        .withRel("cidades")
        );

        cidadeModel.getEstado().add(
                linkTo(methodOn(EstadoController.class).buscar(cidade.getEstado().getId()))
                        .withRel("estado")
        );

        return cidadeModel;

    }

    @Override
    public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(CidadeController.class).listar()).withSelfRel());
    }

    public Cidade toDomain(CidadeInputDTO cidadeInputDTO){
        return mapper.map(cidadeInputDTO, Cidade.class);
    }

}
