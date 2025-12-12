package com.algafoods.core.modelmapper;

import com.algafoods.api.model.EnderecoModel;
import com.algafoods.domain.model.Endereco;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        var mapper = new ModelMapper();

        // FAZ MAPEAMENTO DE METODO ENTRE MODELOS DE DOMINIO E MODELOS DE REPRESENTACAO
        // mapper.createTypeMap(Restaurante.class, RestauranteOutputDTO.class).addMapping(Restaurante::getTaxaFrete, RestauranteOutputDTO::setTaxaFrete);

        // MAPEIA VALOR DO ESTADO PARA DENTRO DO MODELO DE REPRESENTACAO DE CIDADE
        var enderecoToEnderecoModelTypeMap = mapper.createTypeMap(
                Endereco.class,
                EnderecoModel.class
        );

        enderecoToEnderecoModelTypeMap.<String>addMapping(
                endereco -> endereco.getCidade().getEstado().getNome(),
                (enderecoModel, value) -> enderecoModel.getCidade().setEstado(value)
        );

        return mapper;
    }

}
