-- ============================================================
-- SGRO · Tabla de habitaciones (RF-34 a RF-37)
-- Hospedaje Barlen: 8 habitaciones, tarifa base S/ 20.00
-- ============================================================
USE sgro_barlen;

CREATE TABLE IF NOT EXISTS habitacion (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    numero        VARCHAR(10)   NOT NULL UNIQUE,
    tipo          VARCHAR(40)   NOT NULL,                          -- Simple, Doble, Matrimonial, Familiar
    capacidad     INT           NOT NULL DEFAULT 1,
    descripcion   VARCHAR(255),
    estado        ENUM('disponible','ocupada','mantenimiento') NOT NULL DEFAULT 'disponible',
    tarifa        DECIMAL(8,2)  NOT NULL DEFAULT 0.00,
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Carga inicial: 8 habitaciones (solo si la tabla está vacía)
INSERT INTO habitacion (numero, tipo, capacidad, descripcion, estado, tarifa)
SELECT * FROM (
    SELECT '101' AS numero, 'Simple'      AS tipo, 1 AS capacidad, 'Habitación simple con cama de plaza y media, baño privado.'        AS descripcion, 'disponible'    AS estado, 20.00 AS tarifa UNION ALL
    SELECT '102', 'Simple',      1, 'Habitación simple con escritorio y ventana exterior.',                'disponible',    20.00 UNION ALL
    SELECT '103', 'Doble',       2, 'Habitación doble con dos camas individuales y baño privado.',         'disponible',    20.00 UNION ALL
    SELECT '104', 'Doble',       2, 'Habitación doble con vista al patio interior.',                       'ocupada',       20.00 UNION ALL
    SELECT '201', 'Matrimonial', 2, 'Habitación matrimonial con cama de dos plazas y closet.',             'disponible',    20.00 UNION ALL
    SELECT '202', 'Matrimonial', 2, 'Habitación matrimonial con balcón.',                                  'disponible',    20.00 UNION ALL
    SELECT '203', 'Familiar',    4, 'Habitación familiar amplia con cama matrimonial y dos individuales.', 'disponible',    20.00 UNION ALL
    SELECT '204', 'Familiar',    4, 'Habitación familiar en mantenimiento de pintura.',                    'mantenimiento', 20.00
) AS seed
WHERE NOT EXISTS (SELECT 1 FROM habitacion);
