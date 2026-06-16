-- 1. Eliminar los índices parciales que bloquean la conversión de tipo por su cláusula WHERE
DROP INDEX IF EXISTS idx_aa_fallo_usuario;
DROP INDEX IF EXISTS idx_aa_fallo_ip;
DROP INDEX IF EXISTS idx_aa_resultado; -- Eliminamos este también por si genera conflictos con el nuevo tipo

-- 2. Eliminar el CONSTRAINT CHECK que también usa la columna resultado
ALTER TABLE auditoria_accesos
    DROP CONSTRAINT IF EXISTS chk_motivo_fallo;

-- 3. Modificar las columnas convirtiendo los ENUMs a VARCHAR (usando cast explícito a ::text)
ALTER TABLE auditoria_accesos
    ALTER COLUMN tipo_evento TYPE VARCHAR(75) USING (tipo_evento::text),
    ALTER COLUMN resultado TYPE VARCHAR(75) USING (resultado::text),
    ALTER COLUMN motivo_fallo TYPE VARCHAR(75) USING (motivo_fallo::text);

-- 4. Volver a crear el CONSTRAINT CHECK adaptado a VARCHAR
ALTER TABLE auditoria_accesos
    ADD CONSTRAINT chk_motivo_fallo CHECK (
        (resultado = 'exitoso' AND motivo_fallo IS NULL)
        OR
        (resultado = 'fallido' AND motivo_fallo IS NOT NULL)
    );

-- 5. Recrear los índices parciales adaptados a la nueva columna VARCHAR
CREATE INDEX idx_aa_resultado ON auditoria_accesos(resultado);
CREATE INDEX idx_aa_fallo_usuario ON auditoria_accesos(id_usuario, fecha DESC) WHERE resultado = 'fallido';
CREATE INDEX idx_aa_fallo_ip      ON auditoria_accesos(ip_origen,  fecha DESC) WHERE resultado = 'fallido';

-- 6. Borrar definitivamente los tipos ENUM obsoletos del esquema
DROP TYPE IF EXISTS resultado_acceso CASCADE;
DROP TYPE IF EXISTS tipo_evento_acceso CASCADE;
DROP TYPE IF EXISTS motivo_fallo_acceso CASCADE;