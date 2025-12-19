package com.algafoods.api.mappers;

import com.algafoods.api.model.output.pedidos.PedidoOutputDTO;
import com.algafoods.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    private final ModelMapper mapper;


    public PedidoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PedidoOutputDTO toModel(Pedido pedido) {
        return mapper.map(pedido, PedidoOutputDTO.class);
    }
}
