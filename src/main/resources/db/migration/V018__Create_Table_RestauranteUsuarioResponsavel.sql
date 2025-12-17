create table restaurante_usuario_responsavel (
                                             usuario_id bigint not null,
                                             restaurante_id bigint not null
) engine=InnoDB;

alter table restaurante_usuario_responsavel
    add constraint FK7aln770md3358y4olr03hyhh2
        foreign key (usuario_id)
            references usuario (id);

alter table restaurante_forma_pagamento
    add constraint FKa30vowfehtrrw7whjvr8pryvj
        foreign key (restaurante_id)
            references restaurante (id);