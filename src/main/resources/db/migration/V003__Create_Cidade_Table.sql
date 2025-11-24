create table cidade
(
    estado_id bigint       not null,
    id        bigint auto_increment
        primary key,
    nome      varchar(255) not null,
    constraint UKlwg97fq9vkwb5vlqo59krrxoa
        unique (nome),
    constraint FKkworrwk40xj58kevvh3evi500
        foreign key (estado_id) references estado (id)
);