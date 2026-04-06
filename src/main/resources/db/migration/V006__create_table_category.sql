CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,

    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),

    display_order INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE SET NULL
);

CREATE INDEX idx_category_slug ON category(slug);
CREATE INDEX idx_category_parent_id ON category(parent_id);
CREATE INDEX idx_category_active ON category(active);
CREATE INDEX idx_category_display_order ON category(display_order);
