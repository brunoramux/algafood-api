create table restaurante_usuario_responsavel (
                                             usuario_id bigint not null,
                                             restaurante_id bigint not null
) engine=InnoDB;

alter table restaurante_usuario_responsavel
    add constraint FK7aln770mfds8y4olr03hyhh2
        foreign key (usuario_id)
            references usuario (id);

alter table restaurante_usuario_responsavel
    add constraint FKa30vowfedfasrw7whjvr8pryvj
        foreign key (restaurante_id)
            references restaurante (id);