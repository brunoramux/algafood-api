package com.algafoods.core.springdoc;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class OpenApiErrorResponsesConfig {

    @Bean
    public OpenApiCustomizer apiErrorSchemaCustomiser() {
        return openApi -> {

            Schema<?> apiErrorSchema = new Schema<>()
                    .type("object")
                    .addProperty("status", new IntegerSchema().example(404))
                    .addProperty("title", new StringSchema().example("Recurso não encontrado"))
                    .addProperty("detail", new StringSchema().example("Produto não encontrado"))
                    .addProperty("path", new StringSchema().example("/produtos/10"));

            openApi.getComponents()
                    .addSchemas("ApiError", apiErrorSchema);
        };
    }

    @Bean
    public OpenApiCustomizer globalErrorResponsesCustomiser() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {

                    addErrorResponse(operation, "400", "Requisição inválida");
                    addErrorResponse(operation, "404", "Recurso não encontrado");
                    addErrorResponse(operation, "500", "Erro interno");
                })
        );
    }

    private void addErrorResponse(Operation operation,
                                  String status,
                                  String description) {

        io.swagger.v3.oas.models.media.MediaType mediaType =
                new io.swagger.v3.oas.models.media.MediaType()
                        .schema(new Schema<>().$ref("#/components/schemas/ApiError"))
                        .examples(selectExamplesByStatus(status));

        ApiResponse apiResponse = new ApiResponse()
                .description(description)
                .content(new Content().addMediaType(
                        MediaType.APPLICATION_JSON_VALUE,
                        mediaType
                ));

        operation.getResponses().addApiResponse(status, apiResponse);
    }

    private Map<String, Example> selectExamplesByStatus(String status) {
        return switch (status) {
            case "400" -> Map.of(
                    "BadRequest", apiErrorExamples().get("BadRequest")
            );
            case "404" -> Map.of(
                    "NotFound", apiErrorExamples().get("NotFound")
            );
            case "500" -> Map.of(
                    "InternalServerError", apiErrorExamples().get("InternalServerError")
            );
            default -> Map.of();
        };
    }

    private Map<String, Example> apiErrorExamples() {
        Map<String, Example> examples = new HashMap<>();

        examples.put("BadRequest", new Example()
                .summary("Erro de validação")
                .value(Map.of(
                        "status", 400,
                        "title", "Dados inválidos",
                        "detail", "O campo 'preço' é obrigatório",
                        "path", "/produtos"
                )));

        examples.put("NotFound", new Example()
                .summary("Recurso não encontrado")
                .value(Map.of(
                        "status", 404,
                        "title", "Recurso não encontrado",
                        "detail", "Produto não encontrado",
                        "path", "/produtos/10"
                )));

        examples.put("InternalServerError", new Example()
                .summary("Erro interno")
                .value(Map.of(
                        "status", 500,
                        "title", "Erro interno",
                        "detail", "Ocorreu um erro inesperado",
                        "path", "/produtos"
                )));

        return examples;
    }

}