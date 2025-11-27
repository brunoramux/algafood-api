package com.algafoods.api.exceptionhandler;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionHandlerMessage {

    private LocalDateTime dataHora;
    private String mensagem;

}
