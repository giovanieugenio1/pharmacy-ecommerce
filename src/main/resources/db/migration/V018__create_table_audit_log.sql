CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT,
    user_type VARCHAR(20),
    user_email VARCHAR(255),

    action VARCHAR(30) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT,

    old_value TEXT,
    new_value TEXT,

    ip_address VARCHAR(50),
    user_agent TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_log_user_id ON audit_log(user_id);
CREATE INDEX idx_audit_log_entity_type ON audit_log(entity_type);
CREATE INDEX idx_audit_log_entity_id ON audit_log(entity_id);
CREATE INDEX idx_audit_log_action ON audit_log(action);
CREATE INDEX idx_audit_log_created_at ON audit_log(created_at DESC);
