package com.algafoods.infra.repository.spec;

import com.algafoods.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

// UTILIZANDO ESPECIFICACOES PARA BUSCA COM CRITERIOS
// CLASSE QUE CONTEM TODAS AS ESPECIFICACOES DE CONSULTA PARA A ENTIDADE RESTAURANTES
public class RestaurantesSpecs {

    // CRIAR ESPECIFICACOES USANDO FUNCOES LAMBDA. ANTERIORMENTE TINHAMOS CRIADO CADA ESPECIFICACAO EM UM ARQUIVO DIFERENTE
    // RESTAURANTE REPOSITORY DEVE EXTENDER A CLASSE JpaSpecificationExecutor
    public static Specification<Restaurante> comFreteGratis() {
        return ((root, query, builder) ->
                builder.equal(root.get("taxaFrete"), BigDecimal.ZERO));
    }

    public static Specification<Restaurante> comNomeSemelhante(String nome) {
        return ((root, query, builder) ->
                builder.like(root.get("nome"), "%"  + nome + "%"));
    }

}
