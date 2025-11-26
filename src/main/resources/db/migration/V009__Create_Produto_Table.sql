create table produto (
     ativo bit not null,
     preco decimal(38,2) not null,
     data_atualizacao datetime not null,
     data_cadastro datetime not null,
     id bigint not null auto_increment,
     restaurante_id bigint not null,
     descricao varchar(255) not null,
     nome varchar(255) not null,
     primary key (id)
) engine=InnoDB;

alter table produto
    add constraint FKb9jhjyghjcn25guim7q4pt8qx
        foreign key (restaurante_id)
            references restaurante (id);