package com.algafoods.api.infra.repository;

import com.algafoods.api.domain.model.Restaurante;
import com.algafoods.api.domain.repository.RestauranteRepository;
import com.algafoods.api.domain.repository.RestauranteRepositoryQueries;
import com.algafoods.api.infra.repository.spec.RestaurantesSpecs;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    @Lazy
    private RestauranteRepository restauranteRepository;

    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
          // SETANDO PARAMETROS DINAMICAMENTE
//        var jpql = new StringBuilder();
//        jpql.append("from Restaurante where 0 = 0 ");
//
//        var parametros = new HashMap<String, Object>();
//
//        if(StringUtils.hasLength(nome)) {
//            jpql.append(" AND nome LIKE :nome ");
//            parametros.put("nome", "%"+nome+"%");
//        }
//
//        if(taxaFreteInicial != null) {
//            jpql.append("and taxaFrete >= :taxaFreteInicial ");
//            parametros.put("taxaFreteInicial", taxaFreteInicial);
//        }
//
//        if(taxaFreteFinal != null) {
//            jpql.append("and taxaFrete <= :taxaFreteFinal ");
//            parametros.put("taxaFreteFinal", taxaFreteFinal);
//        }
//
//        TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);
//
//       for(var parametro : parametros.entrySet()) {
//          query.setParameter(parametro.getKey(), parametro.getValue());
//       }
//       parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

        // SETANDO PARAMETROS UTILIZANDO PREDICATES
        // CRIAR BUILDER
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        // CRIAR CRITERIA
        CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);

        // CRIA ROOT
        Root<Restaurante> root = criteria.from(Restaurante.class);

        // CRIA LISTA DE PREDICATES DINAMICAMENTE
        var predicates = new ArrayList<Predicate>();

        if(StringUtils.hasText(nome)){
            predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
        }

        if(taxaFreteInicial != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
        }

        if(taxaFreteFinal != null){
            predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
        }

        // ADICIONA O ARRAY DE PREDICATES NO WHERE DO CRITERIA
        criteria.where(predicates.toArray(new Predicate[0]));

        // EXECUTA A QUERY USANDO CRITERIA
        TypedQuery<Restaurante> query = manager.createQuery(criteria);

        // RETORNA O RESULTADO
        return query.getResultList();
    }

    @Override
    public List<Restaurante> findFreteGratis(String nome) {
        List<Restaurante> restaurantes = null;

        if(StringUtils.hasText(nome)){
            restaurantes = restauranteRepository.findAll(RestaurantesSpecs.comFreteGratis().and(RestaurantesSpecs.comNomeSemelhante(nome)));
        } else {
            restaurantes = restauranteRepository.findAll(RestaurantesSpecs.comFreteGratis());
        };

        return restaurantes;
    }
}
