//package com.algafoods.infra.repository.old;
//
//import com.algafoods.domain.model.Cidade;
//import com.algafoods.domain.repository.CidadeRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public class CidadeRepositoryImplementation implements CidadeRepository {
//
//    @PersistenceContext
//    private EntityManager manager;
//
//    @Override
//    public List<Cidade> listar() {
//        return manager.createQuery("from Cidade", Cidade.class).getResultList();
//    }
//
//    @Override
//    public Cidade buscarPorNome(String nome) {
//        return manager.find(Cidade.class, nome);
//    }
//
//    @Override
//    public Cidade buscarPorId(Long id) {
//        return manager.find(Cidade.class, id);
//    }
//
//    @Transactional
//    @Override
//    public Cidade salvar(Cidade cidade) {
//        return manager.merge(cidade);
//    }
//
//    @Transactional
//    @Override
//    public void remover(Cidade cidade) {
//        cidade = buscarPorId(cidade.getId());
//        manager.remove(cidade);
//    }
//}
