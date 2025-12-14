package com.algafoods.domain.repository;

import com.algafoods.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
}
