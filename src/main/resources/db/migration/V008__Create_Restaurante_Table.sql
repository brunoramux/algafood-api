create table restaurante (
     taxa_frete decimal(38,2) not null,
     cozinha_id bigint not null,
     data_atualizacao datetime not null,
     data_cadastro datetime not null,
     endereco_cidade_id bigint,
     id bigint not null auto_increment,
     endereco_bairro varchar(255),
     endereco_cep varchar(255),
     endereco_complemento varchar(255),
     endereco_logradouro varchar(255),
     endereco_numero varchar(255),
     nome varchar(255),
     primary key (id)
) engine=InnoDB;

alter table restaurante
    add constraint FK76grk4roudh659skcgbnanthi
        foreign key (cozinha_id)
            references cozinha (id);

alter table restaurante
    add constraint FKbc0tm7hnvc96d8e7e2ulb05yw
        foreign key (endereco_cidade_id)
            references cidade (id);