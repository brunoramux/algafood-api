package com.algafoods.domain.service;

import com.algafoods.api.mappers.PedidoResumidoMapper;
import com.algafoods.api.model.output.pedidos.PedidoResumidoOutputDTO;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.exception.FormaPagamentoEmPedidoException;
import com.algafoods.domain.model.*;
import com.algafoods.domain.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoResumidoMapper pedidoMapper;
    private final CidadeService cidadeService;
    private final UsuarioService usuarioService;
    private final RestauranteService restauranteService;
    private final FormaPagamentoService formaPagamentoService;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository pedidoRepository, PedidoResumidoMapper pedidoMapper, CidadeService cidadeService, UsuarioService usuarioService, RestauranteService restauranteService, FormaPagamentoService formaPagamentoService, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.cidadeService = cidadeService;
        this.usuarioService = usuarioService;
        this.restauranteService = restauranteService;
        this.formaPagamentoService = formaPagamentoService;
        this.produtoService = produtoService;
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
    }

    public Page<PedidoResumidoOutputDTO> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable).map(pedidoMapper::toModel);
    }

    @Transactional
    public Pedido emitirPedido(Pedido pedido) {
        validarPedido(pedido);
        validarItemPedido(pedido);

        pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
        pedido.calcularValorTotal();

        return pedidoRepository.save(pedido);
    }

    public void validarPedido(Pedido pedido) {
        Cidade cidade = cidadeService.find(pedido.getEnderecoEntrega().getCidade().getId());
        Usuario usuario = usuarioService.find(pedido.getCliente().getId());
        Restaurante restaurante = restauranteService.find(pedido.getRestaurante().getId());
        FormaPagamento formaPagamento = formaPagamentoService.getFormaPagamentoById(pedido.getFormaPagamento().getId());

        pedido.getEnderecoEntrega().setCidade(cidade);
        pedido.setCliente(usuario);
        pedido.setRestaurante(restaurante);
        pedido.setFormaPagamento(formaPagamento);

        if(restaurante.naoAceitaFormaPagamento(formaPagamento)) {
            throw new FormaPagamentoEmPedidoException("Forma de pagamento escolhida não é aceita pelo Restaurante");
        }
    }

    public void validarItemPedido(Pedido pedido) {
        pedido.getItens().forEach(item -> {
            Produto produto = produtoService.find(pedido.getRestaurante().getId(), item.getProduto().getId());
            item.setPrecoUnitario(produto.getPreco());
            item.setProduto(produto);
            item.setPedido(pedido);
        });
    }
}
