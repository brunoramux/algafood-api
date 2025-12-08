package com.algafoods.api.model.mixin;

import com.algafoods.domain.model.Cozinha;
import com.algafoods.domain.model.Endereco;
import com.algafoods.domain.model.FormaPagamento;
import com.algafoods.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

// Classe para definição de anotações do Jackson. Para funcionamento classe JacksonMixinModule deve ser configurada para conectar
// classe Restaurante com classe RestauranteMixin
public class RestauranteMixin {

    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "name"})
    private Cozinha cozinha;

    @JsonIgnore
    private Endereco endereco;

    private OffsetDateTime dataCadastro;

    private OffsetDateTime dataAtualizacao;

    @JsonIgnore
    private List<FormaPagamento> formasPagamento = new ArrayList<>();

    @JsonIgnore
    private List<Produto> produtos = new ArrayList<>();
}
