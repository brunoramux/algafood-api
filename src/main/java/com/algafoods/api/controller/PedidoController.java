package com.algafoods.api.controller;

import com.algafoods.api.mappers.PedidoMapper;
import com.algafoods.api.mappers.PedidoResumidoMapper;
import com.algafoods.api.model.output.pedidos.PedidoOutputDTO;
import com.algafoods.api.model.output.pedidos.PedidoResumidoOutputDTO;
import com.algafoods.domain.model.Pedido;
import com.algafoods.domain.service.PedidoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;
    private final PedidoResumidoMapper pedidoResumidoMapper;


    public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper, PedidoResumidoMapper pedidoResumidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
        this.pedidoResumidoMapper = pedidoResumidoMapper;
    }

    @GetMapping
    public Set<PedidoResumidoOutputDTO> listar() {
        List<Pedido> pedidos = pedidoService.findAll();

        return pedidos.stream()
                .map(pedidoResumidoMapper::toModel)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{pedidoId}")
    public PedidoOutputDTO buscar(
            @PathVariable Long pedidoId
    ) {
        Pedido pedido = pedidoService.findById(pedidoId);

        return pedidoMapper.toModel(pedido);
    }
}
