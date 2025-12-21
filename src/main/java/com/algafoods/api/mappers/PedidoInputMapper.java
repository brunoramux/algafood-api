package com.algafoods.api.mappers;

import com.algafoods.api.model.input.pedidos.PedidoInputDTO;
import com.algafoods.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PedidoInputMapper {

    private final ModelMapper modelMapper;

    public PedidoInputMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Pedido toDomain(PedidoInputDTO pedidoInputDTO) {
        return modelMapper.map(pedidoInputDTO, Pedido.class);
    }

}