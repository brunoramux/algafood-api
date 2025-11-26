create table restaurante_forma_pagamento (
     forma_pagamento_id bigint not null,
     restaurante_id bigint not null
) engine=InnoDB;

alter table restaurante_forma_pagamento
    add constraint FK7aln770m80358y4olr03hyhh2
        foreign key (forma_pagamento_id)
            references forma_pagamento (id);

alter table restaurante_forma_pagamento
    add constraint FKa30vowfejemkw7whjvr8pryvj
        foreign key (restaurante_id)
            references restaurante (id);