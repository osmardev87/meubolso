CREATE TABLE transacoes
(
    id             UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    conta_id       UUID           NOT NULL,
    tipo           VARCHAR(20)    NOT NULL,
    valor          NUMERIC(12, 2) NOT NULL,
    descricao      VARCHAR(255),
    categoria      VARCHAR(100),
    data_transacao TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    efetuada       BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Relacionamento com conta
    CONSTRAINT fk_transacoes_conta
        FOREIGN KEY (conta_id)
            REFERENCES contas (id)
            ON DELETE CASCADE,

    -- Validação dos tipos de transação
    CONSTRAINT chk_transacoes_tipo
        CHECK (tipo IN (
                        'ENTRADA',
                        'SAIDA',
            )),

    -- Valor nunca pode ser negativo — o tipo define o sentido
    CONSTRAINT chk_transacoes_valor_positivo
        CHECK (valor > 0)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_transacoes_conta_id
    ON transacoes (conta_id);

CREATE INDEX IF NOT EXISTS idx_transacoes_data_transacao
    ON transacoes (data_transacao DESC);

CREATE INDEX IF NOT EXISTS idx_transacoes_tipo
    ON transacoes (tipo);

CREATE INDEX IF NOT EXISTS idx_transacoes_categoria
    ON transacoes (categoria);