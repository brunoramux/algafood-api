package com.algafoods.api.model.output;

import com.algafoods.api.model.CozinhaModel;
import com.algafoods.api.model.EnderecoModel;
import com.algafoods.domain.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class RestauranteOutputDTO {

    private Long id;

    private String nome;

    private BigDecimal taxaFrete;

    private Boolean ativo;

    private Boolean aberto;

    private CozinhaModel cozinha;

    private EnderecoModel endereco;

    private Set<UsuarioResponsavelRestauranteOutputDTO> usuarios;

}
