create table grupo_permissao (
     grupo_id bigint not null,
     permissao_id bigint not null
) engine=InnoDB;

alter table grupo_permissao
    add constraint FKh21kiw0y0hxg6birmdf2ef6vy
        foreign key (permissao_id)
            references permissao (id);

alter table grupo_permissao
    add constraint FKta4si8vh3f4jo3bsslvkscc2m
        foreign key (grupo_id)
            references grupo (id);