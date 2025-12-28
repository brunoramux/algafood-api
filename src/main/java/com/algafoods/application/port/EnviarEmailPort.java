package com.algafoods.application.port;

public interface EnviarEmailPort {
    public void enviarEmail(String para, String assunto, String corpoHtml);
}
