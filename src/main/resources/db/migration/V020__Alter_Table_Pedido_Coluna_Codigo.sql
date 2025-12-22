ALTER TABLE pedido ADD codigo VARCHAR(36)  NOT NULL AFTER id;

ALTER TABLE pedido ADD CONSTRAINT uk_pedido_codigo  UNIQUE (codigo);