package com.algafoods.api.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@UtilityClass
public class ResourceUriHelper {

    public static void addUriInResponseHeader(Object resourceId) {
        // Adiciona o header Location na resposta com a URI da nova cidade criada seguindo padroes REST HATEOAS
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(resourceId).toUri();

        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();

        assert response != null;
        response.setHeader(HttpHeaders.LOCATION, uri.toString());

    }

}
