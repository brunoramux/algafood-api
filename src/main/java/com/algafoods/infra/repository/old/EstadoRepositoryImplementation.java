//package com.algafoods.infra.repository.old;
//
//import com.algafoods.domain.model.Estado;
//import com.algafoods.domain.repository.EstadoRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public class EstadoRepositoryImplementation implements EstadoRepository {
//
//    @PersistenceContext
//    private EntityManager manager;
//
//    @Override
//    public List<Estado> listar() {
//        return manager.createQuery("from Estado", Estado.class).getResultList();
//    }
//
//    @Override
//    public Estado buscarPorId(Long id) {
//        return manager.find(Estado.class, id);
//    }
//
//    @Transactional
//    @Override
//    public Estado salvar(Estado estado) {
//        return manager.merge(estado);
//    }
//
//    @Transactional
//    @Override
//    public void remover(Estado estado) {
//        estado = buscarPorId(estado.getId());
//        manager.remove(estado);
//    }
//}
