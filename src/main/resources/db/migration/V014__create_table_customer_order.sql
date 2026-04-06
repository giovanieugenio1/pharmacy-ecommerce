CREATE TABLE customer_order (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,

    order_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'CREATED',

    subtotal_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    shipping_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,

    payment_method VARCHAR(20) NOT NULL,
    delivery_method VARCHAR(20) NOT NULL,

    -- Address snapshot
    address_snapshot_json TEXT NOT NULL,

    -- Delivery tracking
    estimated_delivery_date DATE,
    delivered_at TIMESTAMP,

    notes TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (store_id) REFERENCES store(id)
);

CREATE INDEX idx_customer_order_customer_id ON customer_order(customer_id);
CREATE INDEX idx_customer_order_store_id ON customer_order(store_id);
CREATE INDEX idx_customer_order_order_number ON customer_order(order_number);
CREATE INDEX idx_customer_order_status ON customer_order(status);
CREATE INDEX idx_customer_order_created_at ON customer_order(created_at DESC);
