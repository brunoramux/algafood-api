package com.algafoods.api.exceptionhandler;

import com.algafoods.api.domain.exception.EntidadeEmUsoException;
import com.algafoods.api.domain.exception.EntidadeNaoEncontradaException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        // ENUM com dados para cada tipo de exception
        ExceptionType exceptionType = ExceptionType.ENTIDADE_NAO_ENCONTRADA;
        String detail = ex.getMessage();

        // Cria a mensagem utilizando o metodo
        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionType exceptionType = ExceptionType.ENTIDADE_EM_USO;
        String detail = ex.getMessage();

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);
    }

    // Metodo para retorno de erros de API
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        if(body == null) {
            body = ExceptionHandlerMessage.builder()
                    .title(ex.getMessage())
                    .status(statusCode.value())
                    .build();
        } else if(body instanceof String) {
            body = ExceptionHandlerMessage.builder()
                    .title((String) body)
                    .status(statusCode.value())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    // Builder de mensagens. Utiliza a classe ExceptionHandlerMessage para criar mensagem de erro no padrão
    private ExceptionHandlerMessage.ExceptionHandlerMessageBuilder createExceptionMessageBuilder(HttpStatus status, ExceptionType type, String detail) {
        return ExceptionHandlerMessage.builder()
                .status(status.value())
                .type(type.getUri())
                .title(type.getTitle())
                .detail(detail);
    }

}
