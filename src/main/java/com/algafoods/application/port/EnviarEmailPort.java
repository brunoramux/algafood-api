package com.algafoods.application.port;

import java.util.Map;

public interface EnviarEmailPort {
    public void enviarEmail(String para, String assunto, String template, Map<String, Object> parametros);
}
