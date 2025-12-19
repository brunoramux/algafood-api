package com.algafoods.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// ValorZeroIncluiDescricao é uma Annotation criada para ser usada apenas em Classes. @Target({ ElementType.TYPE })
// Uma anotation é criada nesse contexto para validações específicas.
// @EqualsAndHashCode(onlyExplicitlyIncluded = true) indica ao Lombok para usar apenas campos espeficios ao criar os metodos equals e hashcode
// @ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;


    @Column(name = "taxa_frete", nullable = false)
    private BigDecimal taxaFrete;

    // FETCH LAZY -> Faz SELECT de Cozinhas apenas se os dados forem necessários na requisição.

    // A propriedade hibernateLazyInitializer é necessária ao usar o Lazy

    // JsonIgnoreProperties é usado para indicar campos que não queremos serializar ou desserializar na API.
    // A propriedade allowGetter é usada para indicarmos que apenas ao enviar JSON - serialização - deve ignorar
    // a propriedade. Ao receber JSON na resposta, deve desserializar normalmente.

    // @Valid faz com que a Bean Validation seja em cascada, ou seja, entre no objeto Cozinha e valide os campos que estão no grupo.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cozinha_id", nullable = false)
    @Valid
    private Cozinha cozinha;

    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
    private List<FormaPagamento> formasPagamento = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "restaurante_usuario_responsavel",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "restaurante")
    private List<Produto> produtos = new ArrayList<>();

    // @Embedded trata-se de anotação para considerar os atributos de endereço como parte da entidade de Restaurantes
    @Embedded
    private Endereco endereco;

    @Column(nullable = false)
    private boolean ativo = Boolean.TRUE;

    @Column(nullable = false)
    private boolean aberto = Boolean.FALSE;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCadastro;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataAtualizacao;

    public void ativar() {
        this.ativo = Boolean.TRUE;
    }

    public void desativar() {
        this.ativo = Boolean.FALSE;
    }

    public void abrir(){
        this.aberto = Boolean.TRUE;
    }

    public void fechar(){
        this.aberto = Boolean.FALSE;
    }

    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public void removerUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
    }

    public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
        return getFormasPagamento().contains(formaPagamento);
    }

    public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
        return !aceitaFormaPagamento(formaPagamento);
    }

}
