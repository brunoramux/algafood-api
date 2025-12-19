package com.algafoods.api.model.output.pedidos;

import com.algafoods.api.model.EnderecoModel;
import com.algafoods.api.model.output.FormaPagamentoOutputDTO;
import com.algafoods.api.model.output.UsuarioOutputDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoOutputDTO {

    private Long id;

    private BigDecimal subTotal;

    private BigDecimal taxaFrete;

    private BigDecimal valorTotal;

    private String status;

    private OffsetDateTime dataCriacao;

    private OffsetDateTime dataConfirmacao;

    private OffsetDateTime dataEntrega;

    private OffsetDateTime dataCancelamento;

    private RestauranteResumoOutputDTO restaurante;

    private UsuarioOutputDTO cliente;

    private FormaPagamentoOutputDTO formaPagamento;

    private EnderecoModel enderecoEntrega;

    private List<ItemPedidoOutputDTO> itens;

}
