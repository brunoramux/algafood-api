package com.algafoods.infra.repository;

import com.algafoods.domain.repository.CustomJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Optional;

public class CustomJpaRepositoryImpl<T, ID>
        extends SimpleJpaRepository<T, ID>
        implements CustomJpaRepository<T, ID> {

    private EntityManager manager;

    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.manager = entityManager;
    }

    @Override
    public Optional<T> buscarPrimeiro() {
        // getDomainClass().getName() -> RETORNA QUAL A CLASSE DINAMICA ESTA SENDO UTILIZADA.
        var jpql = "from " + getDomainClass().getName();

        T entity = manager.createQuery(jpql, getDomainClass())
                .setMaxResults(1)
                .getSingleResult();

        return Optional.ofNullable(entity);
    }

    // METODO UTILIZADO PARA INDICAR AO JPA QUE DEIXE DE GERENCIAR UMA ENTIDADE ESPECIFICA
    @Override
    public void detach(T entity) {
        manager.detach(entity);
    }
}
