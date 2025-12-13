package com.algafoods.domain.service;


import com.algafoods.domain.exception.EntidadeEmUsoException;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.exception.SenhaInvalidaException;
import com.algafoods.domain.model.Grupo;
import com.algafoods.domain.model.Usuario;
import com.algafoods.domain.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    public static final String MENSAGEM_USUARIO_NAO_ENCONTRADO = "Usuário com o código %d não encontrado.";
    public static final String MENSAGEM_USUARIO_EM_USO = "Usuário em uso.";

    private final UsuarioRepository repository;
    private final GrupoService grupoService;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, GrupoService grupoService, PasswordEncoder encoder) {
        this.repository = repository;
        this.grupoService = grupoService;
        this.encoder = encoder;
    }

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    public Usuario find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_USUARIO_NAO_ENCONTRADO,  id)));
    }

    @Transactional
    public Usuario create(Usuario usuario) {
        List<Grupo> grupos = usuario.getGrupos().stream()
                .map(grupo -> grupoService.find(grupo.getId()))
                .toList();

        usuario.setGrupos(grupos);

        String senhaCriptografada = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return repository.save(usuario);
    }

    @Transactional
    public void delete(Long id) {
        Usuario usuario = this.find(id);

        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(MENSAGEM_USUARIO_EM_USO);
        }

    }

    @Transactional
    public Usuario update(Usuario usuario) {
        return repository.save(usuario);
    }

    @Transactional
    public void alterarSenha(Long id, String senhaAtual, String novaSenha){
        Usuario usuario = this.find(id);

        boolean senhaAtualValida = encoder.matches(senhaAtual, usuario.getSenha());

        if(!senhaAtualValida){
            throw new SenhaInvalidaException("Senha atual inválida.");
        }

        String senhaCriptografada = encoder.encode(novaSenha);
        usuario.setSenha(senhaCriptografada);

        repository.save(usuario);
    }
}
