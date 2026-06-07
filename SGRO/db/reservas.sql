-- ============================================================
-- SGRO · Reservas, detalle y pagos (RF-14 a RF-33)
-- ============================================================
USE sgro_barlen;

CREATE TABLE IF NOT EXISTS reserva (
    id_reserva     INT AUTO_INCREMENT PRIMARY KEY,
    codigo         VARCHAR(20)  NOT NULL UNIQUE,                 -- p. ej. RES-A1B2C3D4
    id_cliente     INT          NOT NULL,
    fecha_entrada  DATE         NOT NULL,
    fecha_salida   DATE         NOT NULL,
    num_huespedes  INT          NOT NULL DEFAULT 1,
    noches         INT          NOT NULL,
    monto_total    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado         ENUM('pendiente','pagada','cancelada') NOT NULL DEFAULT 'pendiente',
    fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reserva_cliente
        FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS detalle_reserva (
    id_detalle    INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva    INT           NOT NULL,
    id_habitacion INT           NOT NULL,
    noches        INT           NOT NULL,
    subtotal      DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_reserva
        FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_habitacion
        FOREIGN KEY (id_habitacion) REFERENCES habitacion(id_habitacion)
);

CREATE TABLE IF NOT EXISTS pago (
    id_pago     INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva  INT           NOT NULL,
    monto       DECIMAL(10,2) NOT NULL,
    medio_pago  VARCHAR(30)   NOT NULL,                          -- tarjeta | yape | efectivo
    estado_pago ENUM('aprobado','rechazado') NOT NULL,
    fecha_pago  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pago_reserva
        FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva) ON DELETE CASCADE
);
