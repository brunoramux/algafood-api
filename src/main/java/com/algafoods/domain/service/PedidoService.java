package com.algafoods.domain.service;

import com.algafoods.api.mappers.PedidoResumidoMapper;
import com.algafoods.api.model.output.pedidos.PedidoResumidoOutputDTO;
import com.algafoods.application.usecases.EnviarEmailUseCase;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.exception.FormaPagamentoEmPedidoException;
import com.algafoods.domain.exception.StatusPedidoInvalidoException;
import com.algafoods.domain.model.*;
import com.algafoods.domain.repository.PedidoRepository;
import com.algafoods.domain.repository.filter.PedidoFilter;
import com.algafoods.infra.persistence.spec.PedidoSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoResumidoMapper pedidoMapper;
    private final CidadeService cidadeService;
    private final UsuarioService usuarioService;
    private final RestauranteService restauranteService;
    private final FormaPagamentoService formaPagamentoService;
    private final ProdutoService produtoService;
    private final EnviarEmailUseCase enviarEmailUseCase;

    public PedidoService(PedidoRepository pedidoRepository, PedidoResumidoMapper pedidoMapper, CidadeService cidadeService, UsuarioService usuarioService, RestauranteService restauranteService, FormaPagamentoService formaPagamentoService, ProdutoService produtoService, EnviarEmailUseCase enviarEmailUseCase) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.cidadeService = cidadeService;
        this.usuarioService = usuarioService;
        this.restauranteService = restauranteService;
        this.formaPagamentoService = formaPagamentoService;
        this.produtoService = produtoService;
        this.enviarEmailUseCase = enviarEmailUseCase;
    }

    public Pedido findByCodigo(String codigo) {
        return pedidoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
    }


    public Page<PedidoResumidoOutputDTO> findAll(Pageable pageable, PedidoFilter filter) {
        return pedidoRepository.findAll(PedidoSpecs.useFilter(filter), pageable).map(pedidoMapper::toModel);
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

    @Transactional
    public void confirmarPedido(String codigo) {
        Pedido pedido = this.findByCodigo(codigo);
        if(!pedido.getStatus().equals(StatusPedido.CRIADO)){
            throw new StatusPedidoInvalidoException(
                    String.format("Pedido com o status %s não pode ser confirmado.", pedido.getStatus())
            );
        }

        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setDataConfirmacao(OffsetDateTime.now());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("pedido", pedido);

        enviarEmailUseCase.execute(
                "bruno.lemos@live.com",
                "Confirmação de Pedido",
                "pedido-confirmado.html",
                parametros
        );
    }

    @Transactional
    public void entregarPedido(String codigo) {
        Pedido pedido = this.findByCodigo(codigo);
        if(pedido.getStatus().equals(StatusPedido.CANCELADO)){
            throw new StatusPedidoInvalidoException("Pedido cancelado.");
        }

        pedido.setStatus(StatusPedido.ENTREGUE);
        pedido.setDataEntrega(OffsetDateTime.now());
    }

    @Transactional
    public void cancelarPedido(String codigo) {
        Pedido pedido = this.findByCodigo(codigo);
        if(pedido.getStatus().equals(StatusPedido.CANCELADO)){
            throw new StatusPedidoInvalidoException("Pedido já cancelado.");
        }
        if(pedido.getStatus().equals(StatusPedido.ENTREGUE)){
            throw new StatusPedidoInvalidoException("Pedido com o status entregue.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setDataCancelamento(OffsetDateTime.now());
    }
}
