package com.algafoods.api.model.input.pedidos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoEmPedidoInputDTO {

    @NotNull
    private Long id;

}
