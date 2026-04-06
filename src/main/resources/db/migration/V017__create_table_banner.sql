CREATE TABLE banner (
    id BIGSERIAL PRIMARY KEY,

    title VARCHAR(255) NOT NULL,
    subtitle VARCHAR(255),
    image_url VARCHAR(500) NOT NULL,
    mobile_image_url VARCHAR(500),
    target_url VARCHAR(500),

    position VARCHAR(30) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,

    active BOOLEAN NOT NULL DEFAULT TRUE,
    start_at TIMESTAMP,
    end_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_banner_position ON banner(position);
CREATE INDEX idx_banner_active ON banner(active);
CREATE INDEX idx_banner_display_order ON banner(display_order);
CREATE INDEX idx_banner_dates ON banner(start_at, end_at);
