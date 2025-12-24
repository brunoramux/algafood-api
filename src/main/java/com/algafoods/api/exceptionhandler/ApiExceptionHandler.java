package com.algafoods.api.exceptionhandler;

import com.algafoods.domain.exception.EntidadeEmUsoException;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.exception.SenhaInvalidaException;
import com.algafoods.domain.exception.StatusPedidoInvalidoException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import lombok.NonNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** @noinspection ALL*/
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Handle para erros de formatos inválidos no corpo da requisição
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        // TESTA SE O ERRO E DO TIPO FORMATO INVALIDO NA REQUISIÇÃO.
        // EX.: ENVIAR PARÂMETRO DO TIPO ERRADO NO JSON. SE FOR, CHAMA O HANDLE PARA ESSE TIPO DE ERRO - handleInvalidFormatException
        if(rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, statusCode, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindException((PropertyBindingException) rootCause, headers, statusCode, request);
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType exceptionType = ExceptionType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "Corpo da requisição em formato invalido.";

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);
    }


    // Handle para envio de propriedades no corpo da requisição que não existem no escopo da URI
    protected ResponseEntity<Object> handlePropertyBindException(
            PropertyBindingException ex,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {

        String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType exceptionType = ExceptionType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("Propriedade '%s' não deve ser informada.", path);;

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);

    }


    // Handle para o caso de envio de tipos inválidos no corpo da requisição
    protected ResponseEntity<Object> handleInvalidFormatException(
            InvalidFormatException ex,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {

        String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType exceptionType = ExceptionType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu um valor de tipo inválido. Informe um valor do tipo %s. ", path, ex.getTargetType().getSimpleName());

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);

    }

    // Handle da excessão Pedido Inválido
    @ExceptionHandler(StatusPedidoInvalidoException.class)
    protected ResponseEntity<?> handleStatusPedidoInvalidoException(
            StatusPedidoInvalidoException ex,
            WebRequest request
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ExceptionType exceptionType = ExceptionType.STATUS_PEDIDO_INVALIDO;
        String detail = ex.getMessage();

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);

    }

    // Handle da excessão de Entidade não encontrada
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    protected ResponseEntity<?> handleEntidadeNaoEncontradaException(
            EntidadeNaoEncontradaException ex,
            WebRequest request
    ) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        // ENUM com dados para cada tipo de exception
        ExceptionType exceptionType = ExceptionType.ENTIDADE_NAO_ENCONTRADA;
        String detail = ex.getMessage();

        // Cria a mensagem utilizando o metodo
        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);

    }

    // Handle da excessão de Entidade em uso e não pode ser removida
    @ExceptionHandler(EntidadeEmUsoException.class)
    protected ResponseEntity<?> handleEntidadeEmUsoException(
            EntidadeEmUsoException ex,
            WebRequest request
    ) {

        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionType exceptionType = ExceptionType.ENTIDADE_EM_USO;
        String detail = ex.getMessage();

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);

    }

    // Handle da excessão de Senha Inválida
    @ExceptionHandler(SenhaInvalidaException.class)
    protected ResponseEntity<?> handleSenhaInvalidaException(
            SenhaInvalidaException ex,
            WebRequest request
    ) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionType exceptionType = ExceptionType.ERRO_DE_SENHA_INVALIDA;
        String detail = ex.getMessage();

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);

    }

    // Handles de parâmetro inválido enviado na URL
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull TypeMismatchException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);

    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {

        ExceptionType exceptionType = ExceptionType.PARAMETRO_INVALIDO;

        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, headers, status, request);
    }

    // handle para requisições de recursos inexistentes na API
    // Necessário configurar a linha spring.web.resources.add-mappings=false no application.properties para que o Spring não trate como recurso estático e lance a excessão.
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {

        ExceptionType exceptionType = ExceptionType.RECURSO_NAO_ENCONTRADO;
        HttpStatus status = HttpStatus.NOT_FOUND;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.",
                ex.getRequestURL());

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, headers, status, request);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex,
                                                 WebRequest request
    ) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionType exceptionType = ExceptionType.ERRO_DE_SISTEMA;
        String detail = "Ocorreu um erro interno inesperado no sistema. "
                + "Tente novamente e se o problema persistir, entre em contato "
                + "com o administrador do sistema.";

        // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
        // fazendo logging) para mostrar a stacktrace no console
        // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
        // para você durante, especialmente na fase de desenvolvimento
        ex.printStackTrace();

        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);
    }

    // Handle de erros de validações nos campos
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionType exceptionType = ExceptionType.ERRO_DE_VALIDACAO;

        // Código pega cada uma das propriedades com erro e cria um objecto contendo { name, message }
        List<ExceptionHandlerMessage.Object> problemFields = ex.getBindingResult().getAllErrors()
                .stream().map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
                    String name = objectError.getObjectName();
                    if (objectError instanceof FieldError){
                        name = ((FieldError)objectError).getField();
                    }
                    return ExceptionHandlerMessage.Object.builder()
                            .name(name)
                            .message(message)
                            .build();
                })
                        .collect(Collectors.toList());

        String detail = "Um ou mais campos receberam valores inválidos. Corrija e tente novamente.";

        // Cria a mensagem e set os objects - campos a serem mostrados com erro na validacao
        ExceptionHandlerMessage message = createExceptionMessageBuilder(status, exceptionType, detail).objects(problemFields).build();

        return handleExceptionInternal(ex, message, new HttpHeaders(), status, request);

    }

    // Metodo builder da mensagem de erro a ser encaminhada na resposta da API.
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {

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
    private ExceptionHandlerMessage.ExceptionHandlerMessageBuilder createExceptionMessageBuilder(
            HttpStatus status,
            ExceptionType type,
            String detail
    ) {

        return ExceptionHandlerMessage.builder()
                .status(status.value())
                .type(type.getUri())
                .title(type.getTitle())
                .detail(detail)
                .timestamp(OffsetDateTime.now());

    }

}
