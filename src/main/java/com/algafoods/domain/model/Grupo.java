package com.algafoods.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Grupo {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToMany
    @JoinTable(name = "grupo_permissao",
               joinColumns = @JoinColumn(name = "grupo_id"),
               inverseJoinColumns = @JoinColumn(name = "permissao_id"))
    private Set<Permissao> permissoes = new HashSet<>();

    public boolean adicionarPermissao(Permissao permissao) {
        return this.getPermissoes().add(permissao);
    }

    public boolean removerPermissao(Permissao permissao) {
        return this.getPermissoes().remove(permissao);
    }
}
