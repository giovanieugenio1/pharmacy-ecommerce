CREATE TABLE admin_user (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    store_id BIGINT,

    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (role_id) REFERENCES role(id),
    FOREIGN KEY (store_id) REFERENCES store(id)
);

CREATE INDEX idx_admin_user_email ON admin_user(email);
CREATE INDEX idx_admin_user_role_id ON admin_user(role_id);
CREATE INDEX idx_admin_user_store_id ON admin_user(store_id);
CREATE INDEX idx_admin_user_active ON admin_user(active);
