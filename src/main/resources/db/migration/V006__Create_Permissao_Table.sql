create table permissao (
                           id bigint not null auto_increment,
                           descricao varchar(255),
                           nome varchar(255),
                           primary key (id)
) engine=InnoDB;