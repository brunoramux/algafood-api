package com.algafoods.api;

import com.algafoods.api.domain.exception.EntidadeEmUsoException;
import com.algafoods.api.domain.model.Cozinha;
import com.algafoods.api.domain.model.Restaurante;
import com.algafoods.api.domain.service.CadastroCozinhaService;
import com.algafoods.api.domain.service.CadastroRestauranteService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CadastroCozinhaIT {

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @LocalServerPort
    private int port;

    @Test
    public void cadastroCozinhaComSucessoTest() {
        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");

        Cozinha newCozinha = cadastroCozinhaService.salvar(cozinha);

        Assertions.assertThat(newCozinha).isNotNull();
        Assertions.assertThat(newCozinha.getId()).isNotNull();
    }

    @Test
    public void shouldNotCreateCozinhaWithoutName() {
        Cozinha cozinha = new Cozinha();

        assertThrows(ConstraintViolationException.class, () -> {
            cadastroCozinhaService.salvar(cozinha);
        });
    }

    @Test
    public void shouldNotDeleteCozinhaInUse() {

        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");

        Cozinha newCozinha = cadastroCozinhaService.salvar(cozinha);

        Restaurante restaurante = new Restaurante();
        restaurante.setCozinha(newCozinha);
        restaurante.setNome("Restaurante de Cozinha Chinesa");
        restaurante.setTaxaFrete(BigDecimal.valueOf(10.0));

        Restaurante newRestaurante = cadastroRestauranteService.create(restaurante);

        assertThrows(EntidadeEmUsoException.class, () -> {
            cadastroCozinhaService.excluir(newCozinha.getId());
        });
    }

    @Test
    public void shouldReturnHttpStatus200WhenGettingCozinhas() {
        RestAssured.given()
                .basePath("/cozinhas")
                .port(port)
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}