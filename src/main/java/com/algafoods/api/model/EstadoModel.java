package com.algafoods.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class EstadoModel extends RepresentationModel<EstadoModel> {
    private Long id;
    private String nome;
}
