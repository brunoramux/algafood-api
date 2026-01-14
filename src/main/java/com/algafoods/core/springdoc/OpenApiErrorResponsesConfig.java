package com.algafoods.core.springdoc;

import com.algafoods.api.exceptionhandler.ExceptionHandlerMessage;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;



@Configuration
public class OpenApiErrorResponsesConfig {

    @Bean
    public OpenApiCustomizer apiErrorSchemaCustomiser() {
        return openApi -> {

            Schema<?> apiErrorSchema =
                    ModelConverters.getInstance()
                            .read(ExceptionHandlerMessage.class)
                            .get("ExceptionHandlerMessage");

            openApi.getComponents()
                    .addSchemas("ExceptionHandlerMessage", apiErrorSchema);
        };
    }

    @Bean
    public OpenApiCustomizer globalErrorResponsesCustomiser() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {

                    addErrorResponse(operation, "500", "Erro interno do servidor.");
                })
        );
    }

    private void addErrorResponse(Operation operation,
                                  String status,
                                  String description) {

        io.swagger.v3.oas.models.media.MediaType mediaType =
                new io.swagger.v3.oas.models.media.MediaType()
                        .schema(new Schema<>().$ref("#/components/schemas/ExceptionHandlerMessage"));

        ApiResponse apiResponse = new ApiResponse()
                .description(description)
                .content(new Content().addMediaType(
                        MediaType.APPLICATION_JSON_VALUE,
                        mediaType
                ));

        operation.getResponses().addApiResponse(status, apiResponse);
    }


}