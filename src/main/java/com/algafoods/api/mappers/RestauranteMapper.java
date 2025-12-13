package com.algafoods.api.mappers;

import com.algafoods.api.model.input.RestauranteInputDTO;
import com.algafoods.api.model.output.RestauranteOutputDTO;
import com.algafoods.domain.model.Cidade;
import com.algafoods.domain.model.Cozinha;
import com.algafoods.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RestauranteMapper {

    private final ModelMapper mapper;

    public RestauranteMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public RestauranteOutputDTO toModel(Restaurante restaurante) {
        return mapper.map(restaurante, RestauranteOutputDTO.class);
    }

    public Restaurante toDomain(RestauranteInputDTO restauranteInputDTO) {
        return mapper.map(restauranteInputDTO, Restaurante.class);
    }

    public void copyToDomainObject(RestauranteInputDTO restauranteInputDTO, Restaurante restaurante) {
        // PARA EVITAR EXCEPTION DO JPA AO ALTERAR ID DA COZINHA
        restaurante.setCozinha(new Cozinha());

        // PARA EVITAR EXCEPTION DO JPA AO ALTERAR ID DA CIDADE
        if (restauranteInputDTO.getEndereco() != null) {
            restaurante.getEndereco().setCidade(new Cidade());
        }

        mapper.map(restauranteInputDTO, restaurante);
    }

    public void copyToModelInputObject(Restaurante restaurante, RestauranteInputDTO restauranteInputDTO) {
        mapper.map(restaurante, restauranteInputDTO);
    }

}
