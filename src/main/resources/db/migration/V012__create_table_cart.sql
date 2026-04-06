CREATE TABLE cart (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT,
    session_id VARCHAR(255),

    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,

    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

CREATE INDEX idx_cart_customer_id ON cart(customer_id);
CREATE INDEX idx_cart_session_id ON cart(session_id);
CREATE INDEX idx_cart_status ON cart(status);
CREATE INDEX idx_cart_expires_at ON cart(expires_at);
