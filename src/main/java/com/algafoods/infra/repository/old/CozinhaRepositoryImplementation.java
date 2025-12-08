//package com.algafoods.api.infra.repository;
//
//import com.algafoods.model.domain.Cozinha;
//import com.algafoods.repository.domain.CozinhaRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public class CozinhaRepositoryImplementation {
//
//    @PersistenceContext
//    private EntityManager manager;
//
//    @Override
//    public List<Cozinha> listar() {
//        return manager.createQuery("from Cozinha", Cozinha.class).getResultList();
//    }
//
//    @Override
//    public List<Cozinha> buscarPorNome(String nome) {
//        return manager.createQuery("from Cozinha where nome like concat('%', :nome, '%')", Cozinha.class)
//                .setParameter("nome", nome).getResultList();
//    }
//
//    @Override
//    public Cozinha buscarPorId(Long id) {
//        return manager.find(Cozinha.class, id);
//    }
//
//    @Transactional
//    @Override
//    public Cozinha salvar(Cozinha cozinha) {
//        return manager.merge(cozinha);
//    }
//
//    @Transactional
//    @Override
//    public void remover(Long id) {
//        Cozinha cozinha = buscarPorId(id);
//
//        if(cozinha == null){
//            throw new EmptyResultDataAccessException(1);
//        }
//
//        manager.remove(cozinha);
//    }
//}
