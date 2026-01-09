package com.algafoods.api.model.output.pedidos;

import com.algafoods.api.model.output.usuarios.UsuarioOutputDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class PedidoResumidoOutputDTO {

    private String codigo;

    private BigDecimal subTotal;

    private BigDecimal taxaFrete;

    private BigDecimal valorTotal;

    private String status;

    private OffsetDateTime dataCriacao;

    private RestauranteResumoOutputDTO restaurante;

    private UsuarioOutputDTO cliente;

    private Integer qtdtItens;

}
