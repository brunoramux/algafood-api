package com.algafoods.api.jpa;

import com.algafoods.api.ApiApplication;
import com.algafoods.api.domain.model.Cozinha;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class BuscaCozinhaMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(ApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CadastroCozinha cadastroCozinha = applicationContext.getBean(CadastroCozinha.class);
        Cozinha cozinha = cadastroCozinha.buscar(4L);

        if (cozinha != null) {
            System.out.println(cozinha.getNome());
        } else {
            System.out.println("Cozinha não encontrada.");
        }

    }
}
