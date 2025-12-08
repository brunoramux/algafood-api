//package com.algafoods.api.infra.repository.old;
//
//import com.algafoods.model.domain.FormaPagamento;
//import com.algafoods.repository.domain.FormaPagamentoRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public class FormaPagamentoRepositoryImplementation implements FormaPagamentoRepository {
//    @PersistenceContext
//    private EntityManager manager;
//
//    @Override
//    public List<FormaPagamento> listar() {
//        return manager.createQuery("from FormaPagamento", FormaPagamento.class).getResultList();
//    }
//
//    @Override
//    public FormaPagamento buscarPorId(Long id) {
//        return manager.find(FormaPagamento.class, id);
//    }
//
//    @Override
//    public FormaPagamento buscarPorNome(String nome) {
//        return manager.find(FormaPagamento.class, nome);
//    }
//
//    @Transactional
//    @Override
//    public FormaPagamento salvar(FormaPagamento formaPagamento) {
//        return manager.merge(formaPagamento);
//    }
//
//    @Transactional
//    @Override
//    public void remover(FormaPagamento formaPagamento) {
//        formaPagamento = buscarPorId(formaPagamento.getId());
//        manager.remove(formaPagamento);
//    }
//}
