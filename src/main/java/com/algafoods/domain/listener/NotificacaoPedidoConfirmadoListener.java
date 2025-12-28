package com.algafoods.domain.listener;

import com.algafoods.application.usecases.EnviarEmailUseCase;
import com.algafoods.domain.event.PedidoConfirmadoEvent;
import com.algafoods.domain.model.Pedido;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificacaoPedidoConfirmadoListener {

    private final EnviarEmailUseCase enviarEmailUseCase;

    public NotificacaoPedidoConfirmadoListener(EnviarEmailUseCase enviarEmailUseCase) {
        this.enviarEmailUseCase = enviarEmailUseCase;
    }

    @TransactionalEventListener // EXECUTA APOS O COMMIT DA TRANSAÇÃO NO BD
    public void aoConfirmarPedido(PedidoConfirmadoEvent event) {

        Pedido pedido = event.getPedido();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("pedido", pedido);

        enviarEmailUseCase.execute(
                "bruno.lemos@live.com",
                "Confirmação de Pedido",
                "pedido-confirmado.html",
                parametros
        );

    }
}
