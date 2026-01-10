package com.algafoods.api.model.output.usuarios;


import com.algafoods.api.model.GrupoModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;
import java.util.List;

@Relation(collectionRelation = "usuarios")
@Getter
@Setter
public class UsuarioOutputDTO extends RepresentationModel<UsuarioOutputDTO> {

    private Long id;

    private String nome;

    private String email;

    private OffsetDateTime dataCadastro;

    private List<GrupoModel> grupos;

}
