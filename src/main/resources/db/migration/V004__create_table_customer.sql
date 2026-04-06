CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    cpf VARCHAR(14) UNIQUE,
    phone VARCHAR(20),
    birth_date DATE,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_customer_email ON customer(email);
CREATE INDEX idx_customer_cpf ON customer(cpf);
CREATE INDEX idx_customer_active ON customer(active);
