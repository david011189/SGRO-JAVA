-- ============================================================
-- SGRO · Tabla de tokens para recuperación de contraseña (RF-03)
-- ============================================================
USE sgro_barlen;

CREATE TABLE IF NOT EXISTS recuperacion_token (
    id_token    INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT          NOT NULL,
    token       CHAR(64)     NOT NULL UNIQUE,           -- 32 bytes en hexadecimal
    expira      DATETIME     NOT NULL,                  -- caduca 1 hora después de generado
    usado       TINYINT(1)   NOT NULL DEFAULT 0,        -- 1 = ya se usó (no reutilizable)
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recup_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE,
    INDEX idx_token (token)
);
