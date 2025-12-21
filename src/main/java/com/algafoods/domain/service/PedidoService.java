package com.algafoods.domain.service;

import com.algafoods.api.mappers.PedidoResumidoMapper;
import com.algafoods.api.model.output.pedidos.PedidoResumidoOutputDTO;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.Pedido;
import com.algafoods.domain.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoResumidoMapper pedidoMapper;

    public PedidoService(PedidoRepository pedidoRepository, PedidoResumidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
    }

    public Page<PedidoResumidoOutputDTO> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable).map(pedidoMapper::toModel);
    }
}
