CREATE TABLE contas
(
    id            UUID PRIMARY KEY        DEFAULT gen_random_uuid(),

    user_id       UUID           NOT NULL,

    nome          VARCHAR(100)   NOT NULL,

    tipo          VARCHAR(30)    NOT NULL,

    saldo_inicial NUMERIC(12, 2) NOT NULL DEFAULT 0.00,

    ativa         BOOLEAN        NOT NULL DEFAULT TRUE,

    created_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_contas_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT chk_contas_tipo
        CHECK (tipo IN (
                        'CONTA_CORRENTE',
                        'CONTA_POUPANCA',
                        'CARTEIRA_DINHEIRO',
                        'CONTA_PAGAMENTO'
            ))
);

CREATE INDEX idx_contas_user_id ON contas (user_id);