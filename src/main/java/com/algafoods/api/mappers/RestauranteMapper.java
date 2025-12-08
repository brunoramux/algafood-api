package com.algafoods.api.mappers;

import com.algafoods.api.model.CozinhaModel;
import com.algafoods.api.model.input.RestauranteInputDTO;
import com.algafoods.api.model.output.RestauranteOutputDTO;
import com.algafoods.domain.model.Cozinha;
import com.algafoods.domain.model.Restaurante;

public class RestauranteMapper {

    public static RestauranteOutputDTO toModel(Restaurante restaurante) {
        RestauranteOutputDTO restauranteOutputDTO = new RestauranteOutputDTO();
        CozinhaModel cozinhaModel = new CozinhaModel();

        cozinhaModel.setId(restaurante.getCozinha().getId());
        cozinhaModel.setNome(restaurante.getCozinha().getNome());

        restauranteOutputDTO.setId(restaurante.getId());
        restauranteOutputDTO.setNome(restaurante.getNome());
        restauranteOutputDTO.setTaxaFrete(restaurante.getTaxaFrete());
        restauranteOutputDTO.setCozinha(cozinhaModel);

        return restauranteOutputDTO;
    }

    public static Restaurante toDomain(RestauranteInputDTO restauranteInputDTO) {
        Restaurante restaurante = new Restaurante();

        restaurante.setNome(restauranteInputDTO.getNome());
        restaurante.setTaxaFrete(restauranteInputDTO.getTaxaFrete());
        restaurante.setCozinha(new Cozinha());
        restaurante.getCozinha().setId(restauranteInputDTO.getCozinha().getId());

        return restaurante;
    }

}
