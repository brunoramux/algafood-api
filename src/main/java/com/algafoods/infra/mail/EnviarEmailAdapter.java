package com.algafoods.infra.mail;

import com.algafoods.application.port.EnviarEmailPort;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EnviarEmailAdapter implements EnviarEmailPort {

    private final JavaMailSender mailSender;

    public EnviarEmailAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarEmail(String para, String assunto, String corpoHtml) {
        MimeMessage mensagem = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(corpoHtml, true);

            mailSender.send(mensagem);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }

}
