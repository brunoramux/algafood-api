package com.algafoods.application.usecases;

import com.algafoods.application.port.EnviarEmailPort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EnviarEmailUseCase {

    private final EnviarEmailPort enviarEmailPort;

    public EnviarEmailUseCase(EnviarEmailPort enviarEmailPort) {
        this.enviarEmailPort = enviarEmailPort;
    }

    public void execute(String para, String assunto, String template, Map<String, Object> parametros) {
        enviarEmailPort.enviarEmail(para, assunto, template, parametros);
    }
}
