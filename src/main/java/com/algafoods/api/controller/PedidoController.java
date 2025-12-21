package com.algafoods.api.controller;

import com.algafoods.api.mappers.PedidoInputMapper;
import com.algafoods.api.mappers.PedidoMapper;
import com.algafoods.api.model.PageResponseDTO;
import com.algafoods.api.model.input.pedidos.PedidoInputDTO;
import com.algafoods.api.model.output.pedidos.PedidoOutputDTO;
import com.algafoods.api.model.output.pedidos.PedidoResumidoOutputDTO;
import com.algafoods.domain.model.Pedido;
import com.algafoods.domain.model.Usuario;
import com.algafoods.domain.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;
    private final PedidoInputMapper pedidoInputMapper;


    public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper, PedidoInputMapper pedidoInputMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
        this.pedidoInputMapper = pedidoInputMapper;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoOutputDTO adicionar(
            @RequestBody @Valid PedidoInputDTO pedidoInputDTO
    ){
        Pedido pedido = pedidoInputMapper.toDomain(pedidoInputDTO);

        // TODO pegar usuário autenticado
        pedido.setCliente(new Usuario());
        pedido.getCliente().setId(1L);

        Pedido novoPedido = pedidoService.emitirPedido(pedido);

        return pedidoMapper.toModel(novoPedido);
    }
}
