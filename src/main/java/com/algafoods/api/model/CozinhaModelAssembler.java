package com.algafoods.api.model;

import com.algafoods.api.controller.CozinhaController;
import com.algafoods.api.mappers.CozinhaMapper;
import com.algafoods.domain.model.Cozinha;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CozinhaModelAssembler
        extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> {

    private final CozinhaMapper cozinhaMapper;

    public CozinhaModelAssembler(CozinhaMapper cozinhaMapper) {
        super(CozinhaController.class, CozinhaModel.class);
        this.cozinhaMapper = cozinhaMapper;
    }

    @Override
    public CozinhaModel toModel(Cozinha cozinha) {
        CozinhaModel cozinhaModel = cozinhaMapper.toModel(cozinha);

        cozinhaModel.add(linkTo(
                methodOn(CozinhaController.class)
                        .buscar(cozinha.getId()))
                .withSelfRel());

        return cozinhaModel;
    }
}
