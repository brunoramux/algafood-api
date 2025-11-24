create table estado
(
    id   bigint auto_increment
        primary key,
    nome varchar(255) not null,
    constraint UKgfot2y0318rs8hc74ppp0n87p
        unique (nome)
);