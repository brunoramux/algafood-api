package com.algafoods.api.infra.repository;

import com.algafoods.api.domain.model.FormaPagamento;
import com.algafoods.api.domain.model.Permissao;
import com.algafoods.api.domain.repository.PermissaoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PermissaoRepositoryImplementation implements PermissaoRepository {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Permissao> listar() {
        return manager.createQuery("from Permissao", Permissao.class).getResultList();
    }

    @Override
    public Permissao buscarPorId(Long id) {
        return manager.find(Permissao.class, id);
    }


    @Transactional
    @Override
    public Permissao salvar(Permissao permissao) {
        return manager.merge(permissao);
    }

    @Transactional
    @Override
    public void remover(Permissao permissao) {
        permissao = buscarPorId(permissao.getId());
        manager.remove(permissao);
    }
}
