package com.algafoods.api.model.output;


import com.algafoods.api.model.GrupoModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class UsuarioOutputDTO {

    private Long id;

    private String nome;

    private String email;

    private OffsetDateTime dataCadastro;

    private List<GrupoModel> grupos;

}
