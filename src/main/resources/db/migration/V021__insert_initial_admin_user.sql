-- Password: admin123 (BCrypt encoded)
INSERT INTO admin_user (role_id, store_id, name, email, password, active, created_at, updated_at)
SELECT
    r.id,
    s.id,
    'Administrador',
    'admin@farmacia.com',
    '$2a$10$xqH.mHUZQa9wMc1VPd0SJ.W6zfMLKGvL8dpKVFGGGZJGZGH7qKGUW',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM role r, store s
WHERE r.name = 'ADMIN' AND s.is_main = true;
