package com.algafoods.infra.repository.spec;

import com.algafoods.domain.model.Pedido;
import com.algafoods.domain.repository.filter.PedidoFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

// UTILIZANDO ESPECIFICACOES PARA BUSCA COM CRITERIOS DE ACORDO COM LISTA DE PARAMETROS QUE PODEM OU NAO SER PASSADAS NA URL
// CLASSE QUE CONTEM TODAS AS ESPECIFICACOES DE CONSULTA PARA A ENTIDADE PEDIDO
public class PedidoSpecs {

    // VARIAS FUNCOES PODEM SER CRIADAS, CADA UM COM UM CONJUNTO DE FILTROS

    // PEDIDOFILTER E UMA CLASSE DTO COM OS POSSIVEIS PARAMETROS DO FILTRO
    public static Specification<Pedido> useFilter(PedidoFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {

          root.fetch("restaurante").fetch("cozinha");
          root.fetch("cliente");

          var predicates = new ArrayList<Predicate>();

          if(filter.getRestauranteId() != null) {
              predicates.add(criteriaBuilder.equal(root.get("restaurante").get("id"), filter.getRestauranteId()));
          }

          if(filter.getClienteId() != null) {
              predicates.add(criteriaBuilder.equal(root.get("cliente").get("id"), filter.getClienteId()));
          }

          if(filter.getDataCriacaoInicio() != null) {
              OffsetDateTime dataCriacaoInicio = filter.getDataCriacaoInicio().atStartOfDay().atOffset(ZoneOffset.UTC);
              predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataCriacao"), dataCriacaoInicio));
          }

          if(filter.getDataCriacaoFim() != null) {
              OffsetDateTime dataCriacaoFim = OffsetDateTime.of(filter.getDataCriacaoFim(), LocalTime.of(23, 59, 59), ZoneOffset.UTC);
              predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataCriacao"), dataCriacaoFim));
          }

          return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
