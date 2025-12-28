package com.algafoods.api.controller;

import com.algafoods.application.usecases.EnviarEmailUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EnvioEmailController {

    private final EnviarEmailUseCase enviarEmailUseCase;

    public EnvioEmailController(EnviarEmailUseCase enviarEmailUseCase) {
        this.enviarEmailUseCase = enviarEmailUseCase;
    }

    @PostMapping
    public void enviarEmail(
            @RequestParam String para
    ) {
        enviarEmailUseCase.execute(para, "Bem-vindo!", "<h1>Olá 👋</h1>");
    }
}

