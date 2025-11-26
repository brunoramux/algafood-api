create table usuario_grupo (
    grupo_id bigint not null,
    usuario_id bigint not null
) engine=InnoDB;

alter table usuario_grupo
   add constraint FKk30suuy31cq5u36m9am4om9ju
       foreign key (grupo_id)
           references grupo (id);

alter table usuario_grupo
   add constraint FKdofo9es0esuiahyw2q467crxw
       foreign key (usuario_id)
           references usuario (id);