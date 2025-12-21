package com.algafoods.api.model.input.pedidos;

import com.algafoods.api.model.input.EnderecoInputDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoInputDTO {

    @Valid
    @NotNull
    private RestauranteEmPedidoInputDTO restaurante;

    @Valid
    @NotNull
    private EnderecoInputDTO enderecoEntrega;

    @Valid
    @NotNull
    private FormaPagamentoEmPedidoInputDTO formaPagamento;

    @Valid
    @NotNull
    @Size(min = 1)
    private List<ItemPedidoInputDTO> itens;

}
