-- ============================================================
-- TABLA: auditoria_accesos
-- Motor: PostgreSQL 15+
-- ============================================================

CREATE TYPE resultado_acceso AS ENUM (
    'exitoso',
    'fallido'
);

CREATE TYPE tipo_evento_acceso AS ENUM (
    'login',
    'logout',
    'login_2fa',
    'cambio_contrasena',
    'recuperacion_contrasena',
    'bloqueo_cuenta',
    'desbloqueo_cuenta',
    'token_expirado',
    'sesion_expirada'
);

CREATE TYPE motivo_fallo_acceso AS ENUM (
    'contrasena_incorrecta',
    'usuario_no_existe',
    'cuenta_inactiva',
    'cuenta_bloqueada',
    'token_invalido',
    'token_expirado',
    'sesion_invalida',
    'demasiados_intentos',
    'ip_bloqueada',
    'error_interno'
);

CREATE TABLE auditoria_accesos (
    id_auditoria                    BIGSERIAL               PRIMARY KEY,
    id_usuario                      INT                     REFERENCES usuarios(id_usuario) ON DELETE SET NULL,
    correo_intentado                VARCHAR(150),
    tipo_evento                     tipo_evento_acceso      NOT NULL,
    resultado                       resultado_acceso        NOT NULL,
    motivo_fallo                    motivo_fallo_acceso,
    detalle                         TEXT,
    ip_origen                       VARCHAR(45)             NOT NULL,
    user_agent                      VARCHAR(500),
    intentos_fallidos_consecutivos  INT                     NOT NULL DEFAULT 0,
    fecha                           TIMESTAMPTZ             NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_motivo_fallo CHECK (
        (resultado = 'exitoso' AND motivo_fallo IS NULL)
        OR
        (resultado = 'fallido' AND motivo_fallo IS NOT NULL)
    )
);

COMMENT ON TABLE  auditoria_accesos IS 'Registro de eventos de autenticación: exitosos, fallidos y motivo';
COMMENT ON COLUMN auditoria_accesos.correo_intentado IS 'Correo ingresado aunque el usuario no exista en el sistema';
COMMENT ON COLUMN auditoria_accesos.motivo_fallo IS 'Obligatorio cuando resultado = fallido; NULL cuando exitoso';
COMMENT ON COLUMN auditoria_accesos.intentos_fallidos_consecutivos IS 'Contador acumulado al momento del evento para detección de fuerza bruta';
COMMENT ON COLUMN auditoria_accesos.fecha IS 'TIMESTAMPTZ para trazabilidad correcta sin importar el timezone del servidor';

CREATE INDEX idx_aa_usuario  ON auditoria_accesos(id_usuario);
CREATE INDEX idx_aa_ip       ON auditoria_accesos(ip_origen);
CREATE INDEX idx_aa_fecha    ON auditoria_accesos(fecha DESC);
CREATE INDEX idx_aa_resultado ON auditoria_accesos(resultado);
CREATE INDEX idx_aa_fallo_usuario ON auditoria_accesos(id_usuario, fecha DESC) WHERE resultado = 'fallido';
CREATE INDEX idx_aa_fallo_ip      ON auditoria_accesos(ip_origen,  fecha DESC) WHERE resultado = 'fallido';