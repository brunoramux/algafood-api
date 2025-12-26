package com.algafoods.infra.persistence.jpa;

import com.algafoods.application.dto.VendaDiariaDTO;
import com.algafoods.application.filter.VendaDiariaFilter;
import com.algafoods.application.port.RelatorioVendasQueryPort;
import com.algafoods.domain.model.Pedido;
import com.algafoods.domain.model.StatusPedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class RelatorioVendasQueryJpaAdapter implements RelatorioVendasQueryPort {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<VendaDiariaDTO> consultar(VendaDiariaFilter filter, String timeOffSet) {

        var builder = manager.getCriteriaBuilder();
        var query = builder.createQuery(VendaDiariaDTO.class);
        var root = query.from(Pedido.class);

        var predicates = new ArrayList<Predicate>();

        if(filter.getRestauranteId() != null) {
            predicates.add(builder.equal(root.get("restaurante").get("id"), filter.getRestauranteId()));
        }

        if(filter.getDataCriacaoInicio() != null) {
            OffsetDateTime dataCriacaoInicio = filter.getDataCriacaoInicio().atStartOfDay().atOffset(ZoneOffset.UTC);
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), dataCriacaoInicio));
        }

        if(filter.getDataCriacaoFim() != null) {
            OffsetDateTime dataCriacaoFim = OffsetDateTime.of(filter.getDataCriacaoFim(), LocalTime.of(23, 59, 59), ZoneOffset.UTC);
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), dataCriacaoFim));
        }

        predicates.add(root.get("status").in(StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));

        var functionConvertTimeZoneDataCriacao = builder.function(
                "convert_tz",
                Date.class,
                root.get("dataCriacao"),
                builder.literal("+00:00"),
                builder.literal(timeOffSet)
        );

        var functionDateDataCriacao = builder.function(
                "date", LocalDate.class, functionConvertTimeZoneDataCriacao);

        var selection = builder.construct(VendaDiariaDTO.class,
                functionDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));

        query.select(selection);
        query.groupBy(functionDateDataCriacao);
        query.where(predicates.toArray(new Predicate[0]));

        return manager.createQuery(query).getResultList();
    }
}
