CREATE TABLE product_price (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,

    price DECIMAL(10, 2) NOT NULL,
    promotional_price DECIMAL(10, 2),
    promotion_start_at TIMESTAMP,
    promotion_end_at TIMESTAMP,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE,

    UNIQUE (product_id, store_id)
);

CREATE INDEX idx_product_price_product_id ON product_price(product_id);
CREATE INDEX idx_product_price_store_id ON product_price(store_id);
CREATE INDEX idx_product_price_active ON product_price(active);
