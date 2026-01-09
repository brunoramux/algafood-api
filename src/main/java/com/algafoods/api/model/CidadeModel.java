package com.algafoods.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class CidadeModel extends RepresentationModel<CidadeModel> {
    private Long id;
    private String nome;
    private EstadoModel estado;
}
