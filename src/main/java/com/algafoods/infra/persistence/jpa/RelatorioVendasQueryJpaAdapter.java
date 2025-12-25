package com.algafoods.infra.persistence.jpa;

import com.algafoods.application.dto.VendaDiariaDTO;
import com.algafoods.application.filter.VendaDiariaFilter;
import com.algafoods.application.port.RelatorioVendasQueryPort;
import com.algafoods.domain.model.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class RelatorioVendasQueryJpaAdapter implements RelatorioVendasQueryPort {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<VendaDiariaDTO> consultar(VendaDiariaFilter filter) {

        var builder = manager.getCriteriaBuilder();
        var query = builder.createQuery(VendaDiariaDTO.class);
        var root = query.from(Pedido.class);

        var functionDateDataCriacao = builder.function(
                "date", LocalDate.class, root.get("dataCriacao"));

        var selection = builder.construct(VendaDiariaDTO.class,
                functionDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));

        query.select(selection);
        query.groupBy(functionDateDataCriacao);

        return manager.createQuery(query).getResultList();
    }
}
