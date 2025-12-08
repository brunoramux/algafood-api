package com.algafoods.domain.model;

import com.algafoods.core.validation.Groups;
import com.algafoods.core.validation.Multiplo;
import com.algafoods.core.validation.TaxaFrete;
import com.algafoods.core.validation.ValorZeroIncluiDescricao;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

// ValorZeroIncluiDescricao é uma Annotation criada para ser usada apenas em Classes. @Target({ ElementType.TYPE })
// Uma anotation é criada nesse contexto para validações específicas.
// @EqualsAndHashCode(onlyExplicitlyIncluded = true) indica ao Lombok para usar apenas campos espeficios ao criar os metodos equals e hashcode
@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    // @Multiplo e @TaxaFrete -> Anotação de validação para métodos
    // @Column -> Usamos para indicar propriedades para o JPA utilizar na criação da tabela no banco de dados.
    @NotNull
    @TaxaFrete
    @Multiplo(numero = 5)
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
    @NotNull(groups = Groups.IdCozinha.class)
    @Valid
    @ConvertGroup(from = Default.class, to = Groups.IdCozinha.class)
    private Cozinha cozinha;

    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
    private List<FormaPagamento> formasPagamento = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante")
    private List<Produto> produtos = new ArrayList<>();

    // @Embedded trata-se de anotação para considerar os atributos de endereço como parte da entidade de Restaurantes
    @Embedded
    private Endereco endereco;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCadastro;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataAtualizacao;

}
