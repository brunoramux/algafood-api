create table item_pedido (
    id bigint not null auto_increment,
    quantidade bigint not null,
    preco_unitario decimal(38,2) not null,
    preco_total decimal(38,2) not null,
    observacao varchar(255) not null,
    pedido_id bigint not null,
    produto_id bigint not null,

    primary key (id)
);

alter table item_pedido
    add constraint item_pedido_pedido
        foreign key (pedido_id)
            references pedido (id);

alter table item_pedido
    add constraint item_pedido_produto
        foreign key (produto_id)
            references produto (id);