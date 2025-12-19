package com.algafoods.api.controller;

import com.algafoods.api.mappers.PedidoMapper;
import com.algafoods.api.model.output.pedidos.PedidoOutputDTO;
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


    public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
    }

    @GetMapping
    public Set<PedidoOutputDTO> listar() {
        List<Pedido> pedidos = pedidoService.findAll();

        return pedidos.stream()
                .map(pedidoMapper::toModel)
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
