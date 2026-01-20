package com.algafoods.infra.security;

import com.algafoods.domain.model.Usuario;
import com.algafoods.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repository;

    public UserDetailsServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = repository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email)
        );

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .build();
    }
}
