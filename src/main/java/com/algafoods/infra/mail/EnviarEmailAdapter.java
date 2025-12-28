package com.algafoods.infra.mail;

import com.algafoods.application.port.EnviarEmailPort;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.validation.ObjectError;

import java.io.IOException;
import java.util.Map;

@Service
public class EnviarEmailAdapter implements EnviarEmailPort {

    private final JavaMailSender mailSender;
    private final Configuration freemarker;

    public EnviarEmailAdapter(JavaMailSender mailSender, Configuration freemarker) {
        this.mailSender = mailSender;
        this.freemarker = freemarker;
    }

    @Override
    public void enviarEmail(String para, String assunto, String template, Map<String, Object> parametros) {
        MimeMessage mensagem = mailSender.createMimeMessage();

        String corpoHtml = processarTemplate(template, parametros);

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

    private String processarTemplate(String corpoHtml, Map<String, Object> parametros){
        try {
            Template template = freemarker.getTemplate(corpoHtml);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, parametros);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
