CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT,
    brand_id BIGINT,

    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    sku VARCHAR(100) UNIQUE,

    short_description VARCHAR(500),
    full_description TEXT,

    featured BOOLEAN NOT NULL DEFAULT FALSE,
    requires_prescription BOOLEAN NOT NULL DEFAULT FALSE,
    controlled_item BOOLEAN NOT NULL DEFAULT FALSE,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE SET NULL
);

CREATE INDEX idx_product_slug ON product(slug);
CREATE INDEX idx_product_sku ON product(sku);
CREATE INDEX idx_product_category_id ON product(category_id);
CREATE INDEX idx_product_brand_id ON product(brand_id);
CREATE INDEX idx_product_active ON product(active);
CREATE INDEX idx_product_featured ON product(featured);
CREATE INDEX idx_product_name ON product(name);
