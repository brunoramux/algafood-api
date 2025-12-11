package com.algafoods.api;

import com.algafoods.domain.exception.EntidadeEmUsoException;
import com.algafoods.domain.model.Cozinha;
import com.algafoods.domain.model.Restaurante;
import com.algafoods.domain.service.CozinhaService;
import com.algafoods.domain.service.RestauranteService;
import com.algafoods.api.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CadastroCozinhaIT {

    public static final int NONEXISTENT_COZINHA_ID = 100;
    @Autowired
    private CozinhaService cozinhaService;

    @Autowired
    private RestauranteService restauranteService;

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;


    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";

        databaseCleaner.clearTables();
        prepareDatabase();
    }

    @Test
    public void cadastroCozinhaComSucessoTest() {
        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");

        Cozinha newCozinha = cozinhaService.salvar(cozinha);

        Assertions.assertThat(newCozinha).isNotNull();
        Assertions.assertThat(newCozinha.getId()).isNotNull();
    }

    @Test
    public void shouldNotCreateCozinhaWithoutName() {
        Cozinha cozinha = new Cozinha();

        assertThrows(ConstraintViolationException.class, () -> {
            cozinhaService.salvar(cozinha);
        });
    }

    @Test
    public void shouldNotDeleteCozinhaInUse() {

        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");

        Cozinha newCozinha = cozinhaService.salvar(cozinha);

        Restaurante restaurante = new Restaurante();
        restaurante.setCozinha(newCozinha);
        restaurante.setNome("Restaurante de Cozinha Chinesa");
        restaurante.setTaxaFrete(BigDecimal.valueOf(10.0));

        Restaurante newRestaurante = restauranteService.create(restaurante);

        assertThrows(EntidadeEmUsoException.class, () -> {
            cozinhaService.excluir(newCozinha.getId());
        });
    }

    @Test
    public void shouldReturnHttpStatus200WhenGettingCozinhas() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldReturnBodyWithFourCozinhas() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", Matchers.hasSize(1))
                .body("nome", Matchers.hasItems("Chinesa"));
    }

    @Test
    public void shouldCreateCozinhaAndReturnHttpStatus201() {
        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");
        RestAssured.given()
                .body(cozinha)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void shouldBeAbleToGetACozinhaWithId() {
        RestAssured.given()
                .pathParam("id", 1)
                .accept(ContentType.JSON)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo("Chinesa"));
    }

    @Test
    public void shouldReturnNotFoundWhenGettingANonExistentCozinha() {
        RestAssured.given()
                .pathParam("id", NONEXISTENT_COZINHA_ID)
                .accept(ContentType.JSON)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void prepareDatabase() {
        Cozinha newCozinha = new Cozinha();
        newCozinha.setNome("Chinesa");
        Cozinha cozinha = cozinhaService.salvar(newCozinha);
    }
}