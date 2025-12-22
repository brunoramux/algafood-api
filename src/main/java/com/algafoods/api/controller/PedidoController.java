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
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
    public MappingJacksonValue listar(
            @RequestParam(required = false) String fields,
            Pageable pageable
    ) {
        PageResponseDTO<PedidoResumidoOutputDTO> pedidos =  PageResponseDTO.from(pedidoService.findAll(pageable));

        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidos);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());

        if(StringUtils.isNotEmpty(fields)) {
            List<String> fieldsList = new ArrayList<>(
                    Arrays.asList(fields.split(","))
            );
            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fieldsList.toArray(new String[0])));
        }

        pedidosWrapper.setFilters(filterProvider);

        return pedidosWrapper;
    }

    @GetMapping("/{codigo}")
    public PedidoOutputDTO buscar(
            @PathVariable String codigo
    ) {
        Pedido pedido = pedidoService.findByCodigo(codigo);

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

    @PutMapping("/{codigo}/confirmar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmarPedido(@PathVariable String codigo) {
        pedidoService.confirmarPedido(codigo);
    }

    @PutMapping("/{codigo}/entregar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void entregarPedido(@PathVariable String codigo) {
        pedidoService.entregarPedido(codigo);
    }

    @PutMapping("/{codigo}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarPedido(@PathVariable String codigo) {
        pedidoService.cancelarPedido(codigo);
    }
}
