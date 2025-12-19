package com.algafoods.api.mappers;

import com.algafoods.api.model.output.pedidos.PedidoOutputDTO;
import com.algafoods.api.model.output.pedidos.PedidoResumidoOutputDTO;
import com.algafoods.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PedidoResumidoMapper {

    private final ModelMapper mapper;


    public PedidoResumidoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PedidoResumidoOutputDTO toModel(Pedido pedido) {
        PedidoResumidoOutputDTO pedidoDTO = mapper.map(pedido, PedidoResumidoOutputDTO.class);
        pedidoDTO.setQtdtItens(pedido.getItens().size());

        return pedidoDTO;
    }
}
