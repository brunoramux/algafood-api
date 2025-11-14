//package com.algafoods.api.infra.repository;
//
//import com.algafoods.api.domain.model.Cozinha;
//import com.algafoods.api.domain.model.Restaurante;
//import com.algafoods.api.domain.repository.RestauranteRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public class RestauranteRepositoryImplementation implements RestauranteRepository {
//    @PersistenceContext
//    private EntityManager manager;
//
//    @Override
//    public List<Restaurante> listar() {
//        return manager.createQuery("from Restaurante", Restaurante.class).getResultList();
//    }
//
//    @Override
//    public Restaurante buscarPorId(Long id) {
//        return manager.find(Restaurante.class, id);
//    }
//
//    @Transactional
//    @Override
//    public Restaurante salvar(Restaurante restaurante) {
//        return manager.merge(restaurante);
//    }
//
//    @Transactional
//    @Override
//    public void remover(Restaurante restaurante) {
//        restaurante = buscarPorId(restaurante.getId());
//        manager.remove(restaurante);
//    }
//}
