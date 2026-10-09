CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(120) NOT NULL,

    email VARCHAR(180) NOT NULL,

    phone VARCHAR(20),

    password VARCHAR(255) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_users_email UNIQUE (email),

    CONSTRAINT uk_users_phone UNIQUE (phone)
);

CREATE INDEX idx_users_email ON users(email);

CREATE INDEX idx_users_phone ON users(phone);

