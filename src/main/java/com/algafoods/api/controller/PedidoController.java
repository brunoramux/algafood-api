package com.algafoods.api.controller;

import com.algafoods.api.mappers.PedidoMapper;
import com.algafoods.api.mappers.PedidoResumidoMapper;
import com.algafoods.api.model.PageResponseDTO;
import com.algafoods.api.model.output.pedidos.PedidoOutputDTO;
import com.algafoods.api.model.output.pedidos.PedidoResumidoOutputDTO;
import com.algafoods.domain.model.Pedido;
import com.algafoods.domain.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public PageResponseDTO<PedidoResumidoOutputDTO> listar(Pageable pageable) {
        return PageResponseDTO.from(pedidoService.findAll(pageable));
    }

    @GetMapping("/{pedidoId}")
    public PedidoOutputDTO buscar(
            @PathVariable Long pedidoId
    ) {
        Pedido pedido = pedidoService.findById(pedidoId);

        return pedidoMapper.toModel(pedido);
    }
}
