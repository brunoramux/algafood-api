package com.algafoods.api.jpa;

import com.algafoods.api.ApiApplication;
import com.algafoods.api.domain.model.Cozinha;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class InclusaoCozinhaMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(ApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CadastroCozinha cadastroCozinha = applicationContext.getBean(CadastroCozinha.class);

        Cozinha cozinha1 = new Cozinha();
        cozinha1.setNome("BRASILEIRA");

        Cozinha cozinha2 = new Cozinha();
        cozinha2.setNome("ITALIANA");

        cozinha1 = cadastroCozinha.adicionar(cozinha1);
        cozinha2 = cadastroCozinha.adicionar(cozinha2);

        System.out.println(cozinha1.getId());
        System.out.println(cozinha2.getId());
    }
}
