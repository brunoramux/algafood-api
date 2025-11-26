    create table usuario (
        data_cadastro datetime not null,
        id bigint not null auto_increment,
        email varchar(255) not null,
        nome varchar(255) not null,
        senha varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;