-- ============================================================
-- BASE DE DATOS: Plataforma de Gestión Estudiantil Integral
-- Motor: PostgreSQL 15+
-- Proyecto: JACH PERÚ CORP / Arquitectura Empresarial
-- Integrantes: Fernández Falcón C.F. | Huaman Chonta M.J. | Palacios Manrique E.R.
-- ============================================================

-- Crear y seleccionar la base de datos
-- CREATE DATABASE gestion_estudiantil WITH ENCODING='UTF8';
-- \c gestion_estudiantil

-- ============================================================
-- MÓDULO 1: AUTENTICACIÓN Y USUARIOS
-- ============================================================

CREATE TABLE usuarios (
    id_usuario      SERIAL          PRIMARY KEY,
    correo          VARCHAR(150)    NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255)    NOT NULL,
    nombres         VARCHAR(100)    NOT NULL,
    apellidos       VARCHAR(100)    NOT NULL,
    rol             VARCHAR(20)     NOT NULL DEFAULT 'estudiante'
                        CHECK (rol IN ('estudiante', 'docente', 'admin')),
    activo          BOOLEAN         NOT NULL DEFAULT TRUE,
    fecha_registro  TIMESTAMP       NOT NULL DEFAULT NOW(),
    ultimo_acceso   TIMESTAMP
);

COMMENT ON TABLE  usuarios              IS 'Usuarios del sistema con roles diferenciados';
COMMENT ON COLUMN usuarios.rol          IS 'estudiante | docente | admin';
COMMENT ON COLUMN usuarios.contrasena_hash IS 'Hash bcrypt de la contraseña, nunca texto plano';

-- -------------------------------------------------------

CREATE TABLE sesiones (
    id_sesion        SERIAL       PRIMARY KEY,
    id_usuario       INT          NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    token            VARCHAR(255) NOT NULL UNIQUE,
    fecha_inicio     TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_expiracion TIMESTAMP,
    ip_origen        VARCHAR(45),
    activa           BOOLEAN      NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE sesiones IS 'Registro de sesiones activas e historial de accesos';

-- ============================================================
-- MÓDULO 2: CURSOS — AULA VIRTUAL
-- ============================================================

CREATE TABLE cursos (
    id_curso    SERIAL        PRIMARY KEY,
    codigo      VARCHAR(20)   NOT NULL UNIQUE,
    nombre      VARCHAR(150)  NOT NULL,
    descripcion TEXT,
    ciclo       VARCHAR(20)   NOT NULL,
    creditos    SMALLINT      NOT NULL CHECK (creditos > 0),
    id_docente  INT           NOT NULL REFERENCES usuarios(id_usuario),
    activo      BOOLEAN       NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE  cursos           IS 'Cursos académicos del semestre';
COMMENT ON COLUMN cursos.ciclo     IS 'Ej: 2026-I, 2026-II';
COMMENT ON COLUMN cursos.id_docente IS 'Referencia al usuario con rol docente';

-- -------------------------------------------------------

CREATE TABLE matriculas (
    id_matricula   SERIAL       PRIMARY KEY,
    id_estudiante  INT          NOT NULL REFERENCES usuarios(id_usuario) ON DELETE RESTRICT,
    id_curso       INT          NOT NULL REFERENCES cursos(id_curso)     ON DELETE RESTRICT,
    fecha_matricula DATE        NOT NULL DEFAULT CURRENT_DATE,
    estado         VARCHAR(20)  NOT NULL DEFAULT 'activo'
                       CHECK (estado IN ('activo', 'retirado', 'aprobado', 'desaprobado')),
    nota_final     NUMERIC(4,2) CHECK (nota_final BETWEEN 0 AND 20),
    UNIQUE (id_estudiante, id_curso)
);

COMMENT ON TABLE matriculas IS 'Inscripción de estudiantes en cursos; evita duplicados con UNIQUE';

-- -------------------------------------------------------

CREATE TABLE materiales (
    id_material   SERIAL        PRIMARY KEY,
    id_curso      INT           NOT NULL REFERENCES cursos(id_curso) ON DELETE CASCADE,
    titulo        VARCHAR(200)  NOT NULL,
    tipo          VARCHAR(20)   NOT NULL
                      CHECK (tipo IN ('pdf', 'video', 'enlace', 'imagen', 'otro')),
    url_archivo   VARCHAR(500)  NOT NULL,
    tamano_kb     INT           CHECK (tamano_kb > 0),
    fecha_subida  TIMESTAMP     NOT NULL DEFAULT NOW(),
    subido_por    INT           NOT NULL REFERENCES usuarios(id_usuario)
);

COMMENT ON TABLE materiales IS 'Archivos, videos y enlaces de cada curso';

-- ============================================================
-- MÓDULO 3: HORARIO — AGENDA ACADÉMICA
-- ============================================================

CREATE TABLE horarios (
    id_horario   SERIAL       PRIMARY KEY,
    id_curso     INT          NOT NULL REFERENCES cursos(id_curso) ON DELETE CASCADE,
    dia_semana   VARCHAR(10)  NOT NULL
                     CHECK (dia_semana IN ('lunes','martes','miercoles','jueves','viernes','sabado')),
    hora_inicio  TIME         NOT NULL,
    hora_fin     TIME         NOT NULL,
    aula         VARCHAR(30),
    modalidad    VARCHAR(15)  NOT NULL DEFAULT 'presencial'
                     CHECK (modalidad IN ('presencial', 'virtual', 'hibrido')),
    enlace_virtual VARCHAR(300),
    CONSTRAINT chk_horario_horas CHECK (hora_fin > hora_inicio)
);

COMMENT ON TABLE  horarios           IS 'Horario semanal de clases por curso';
COMMENT ON COLUMN horarios.dia_semana IS 'Día de la semana en minúsculas sin tilde';

-- -------------------------------------------------------

CREATE TABLE actividades_academicas (
    id_actividad  SERIAL       PRIMARY KEY,
    id_curso      INT          NOT NULL REFERENCES cursos(id_curso) ON DELETE CASCADE,
    titulo        VARCHAR(200) NOT NULL,
    tipo          VARCHAR(20)  NOT NULL
                      CHECK (tipo IN ('examen', 'entrega', 'evento', 'tarea', 'otro')),
    fecha_hora    TIMESTAMP    NOT NULL,
    descripcion   TEXT,
    visible       BOOLEAN      NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE actividades_academicas IS 'Exámenes, entregas y eventos académicos del calendario';

-- ============================================================
-- MÓDULO 4: PROYECTOS — GESTOR DE PROYECTOS
-- ============================================================

CREATE TABLE proyectos (
    id_proyecto  SERIAL       PRIMARY KEY,
    id_curso     INT          NOT NULL REFERENCES cursos(id_curso) ON DELETE RESTRICT,
    nombre       VARCHAR(200) NOT NULL,
    descripcion  TEXT,
    fecha_inicio DATE         NOT NULL,
    fecha_fin    DATE         NOT NULL,
    estado       VARCHAR(20)  NOT NULL DEFAULT 'pendiente'
                     CHECK (estado IN ('pendiente', 'en_proceso', 'completado', 'cancelado')),
    CONSTRAINT chk_proyecto_fechas CHECK (fecha_fin >= fecha_inicio)
);

COMMENT ON TABLE proyectos IS 'Proyectos académicos asociados a un curso';

-- -------------------------------------------------------

CREATE TABLE proyecto_miembros (
    id_miembro        SERIAL      PRIMARY KEY,
    id_proyecto       INT         NOT NULL REFERENCES proyectos(id_proyecto) ON DELETE CASCADE,
    id_usuario        INT         NOT NULL REFERENCES usuarios(id_usuario)   ON DELETE RESTRICT,
    rol               VARCHAR(20) NOT NULL DEFAULT 'colaborador'
                          CHECK (rol IN ('lider', 'colaborador')),
    fecha_asignacion  DATE        NOT NULL DEFAULT CURRENT_DATE,
    UNIQUE (id_proyecto, id_usuario)
);

COMMENT ON TABLE proyecto_miembros IS 'Integrantes de cada proyecto con su rol';

-- -------------------------------------------------------

CREATE TABLE tareas_proyecto (
    id_tarea      SERIAL       PRIMARY KEY,
    id_proyecto   INT          NOT NULL REFERENCES proyectos(id_proyecto) ON DELETE CASCADE,
    titulo        VARCHAR(200) NOT NULL,
    descripcion   TEXT,
    asignado_a    INT          REFERENCES usuarios(id_usuario) ON DELETE SET NULL,
    fecha_limite  DATE,
    estado        VARCHAR(20)  NOT NULL DEFAULT 'pendiente'
                      CHECK (estado IN ('pendiente', 'en_proceso', 'completada', 'bloqueada')),
    prioridad     VARCHAR(10)  NOT NULL DEFAULT 'media'
                      CHECK (prioridad IN ('baja', 'media', 'alta')),
    fecha_creacion TIMESTAMP   NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE tareas_proyecto IS 'Tareas individuales dentro de un proyecto';

-- -------------------------------------------------------

CREATE TABLE archivos_proyecto (
    id_archivo     SERIAL        PRIMARY KEY,
    id_proyecto    INT           NOT NULL REFERENCES proyectos(id_proyecto) ON DELETE CASCADE,
    subido_por     INT           NOT NULL REFERENCES usuarios(id_usuario),
    nombre_archivo VARCHAR(255)  NOT NULL,
    url_archivo    VARCHAR(500)  NOT NULL,
    tamano_kb      INT           CHECK (tamano_kb > 0),
    fecha_subida   TIMESTAMP     NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE archivos_proyecto IS 'Archivos adjuntos a un proyecto (entregables, evidencias)';

-- ============================================================
-- MÓDULO 5: SOPORTE — CENTRO DE AYUDA
-- ============================================================

CREATE TABLE tickets_soporte (
    id_ticket      SERIAL       PRIMARY KEY,
    id_usuario     INT          NOT NULL REFERENCES usuarios(id_usuario) ON DELETE RESTRICT,
    asunto         VARCHAR(200) NOT NULL,
    descripcion    TEXT         NOT NULL,
    estado         VARCHAR(20)  NOT NULL DEFAULT 'abierto'
                       CHECK (estado IN ('abierto', 'en_proceso', 'cerrado', 'cancelado')),
    fecha_creacion TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_cierre   TIMESTAMP
);

COMMENT ON TABLE tickets_soporte IS 'Solicitudes de soporte técnico o académico';

-- -------------------------------------------------------

CREATE TABLE respuestas_ticket (
    id_respuesta  SERIAL    PRIMARY KEY,
    id_ticket     INT       NOT NULL REFERENCES tickets_soporte(id_ticket) ON DELETE CASCADE,
    id_usuario    INT       NOT NULL REFERENCES usuarios(id_usuario),
    mensaje       TEXT      NOT NULL,
    fecha         TIMESTAMP NOT NULL DEFAULT NOW(),
    es_admin      BOOLEAN   NOT NULL DEFAULT FALSE
);

COMMENT ON TABLE respuestas_ticket IS 'Hilo de mensajes de cada ticket de soporte';

-- -------------------------------------------------------

CREATE TABLE faq (
    id_faq     SERIAL        PRIMARY KEY,
    pregunta   TEXT          NOT NULL,
    respuesta  TEXT          NOT NULL,
    categoria  VARCHAR(80)   NOT NULL,
    orden      SMALLINT      NOT NULL DEFAULT 0,
    visible    BOOLEAN       NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE faq IS 'Preguntas frecuentes del centro de ayuda';

-- ============================================================
-- MÓDULO 6: NOTIFICACIONES Y AUDITORÍA
-- ============================================================

CREATE TABLE notificaciones (
    id_notificacion SERIAL       PRIMARY KEY,
    id_usuario      INT          NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    titulo          VARCHAR(200) NOT NULL,
    mensaje         TEXT         NOT NULL,
    tipo            VARCHAR(20)  NOT NULL DEFAULT 'info'
                        CHECK (tipo IN ('info', 'alerta', 'recordatorio', 'error')),
    leida           BOOLEAN      NOT NULL DEFAULT FALSE,
    fecha           TIMESTAMP    NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE notificaciones IS 'Notificaciones del sistema para cada usuario';

-- -------------------------------------------------------

CREATE TABLE auditoria (
    id_auditoria     SERIAL        PRIMARY KEY,
    id_usuario       INT           REFERENCES usuarios(id_usuario) ON DELETE SET NULL,
    accion           VARCHAR(100)  NOT NULL,
    tabla_afectada   VARCHAR(80)   NOT NULL,
    id_registro      INT,
    datos_anteriores JSONB,
    datos_nuevos     JSONB,
    fecha            TIMESTAMP     NOT NULL DEFAULT NOW(),
    ip_origen        VARCHAR(45)
);

COMMENT ON TABLE auditoria IS 'Registro inmutable de cambios críticos en el sistema';

-- ============================================================
-- ÍNDICES — optimización de consultas frecuentes
-- ============================================================

-- Autenticación
CREATE INDEX idx_usuarios_correo       ON usuarios(correo);
CREATE INDEX idx_sesiones_token        ON sesiones(token);
CREATE INDEX idx_sesiones_usuario      ON sesiones(id_usuario);

-- Cursos
CREATE INDEX idx_cursos_docente        ON cursos(id_docente);
CREATE INDEX idx_matriculas_estudiante ON matriculas(id_estudiante);
CREATE INDEX idx_matriculas_curso      ON matriculas(id_curso);
CREATE INDEX idx_materiales_curso      ON materiales(id_curso);

-- Horario
CREATE INDEX idx_horarios_curso        ON horarios(id_curso);
CREATE INDEX idx_actividades_curso     ON actividades_academicas(id_curso);
CREATE INDEX idx_actividades_fecha     ON actividades_academicas(fecha_hora);

-- Proyectos
CREATE INDEX idx_proyectos_curso       ON proyectos(id_curso);
CREATE INDEX idx_miembros_proyecto     ON proyecto_miembros(id_proyecto);
CREATE INDEX idx_miembros_usuario      ON proyecto_miembros(id_usuario);
CREATE INDEX idx_tareas_proyecto       ON tareas_proyecto(id_proyecto);
CREATE INDEX idx_tareas_asignado       ON tareas_proyecto(asignado_a);
CREATE INDEX idx_archivos_proyecto     ON archivos_proyecto(id_proyecto);

-- Soporte
CREATE INDEX idx_tickets_usuario       ON tickets_soporte(id_usuario);
CREATE INDEX idx_tickets_estado        ON tickets_soporte(estado);
CREATE INDEX idx_respuestas_ticket     ON respuestas_ticket(id_ticket);

-- Notificaciones
CREATE INDEX idx_notificaciones_usuario ON notificaciones(id_usuario);
CREATE INDEX idx_notificaciones_leida   ON notificaciones(id_usuario, leida);

-- Auditoría
CREATE INDEX idx_auditoria_usuario     ON auditoria(id_usuario);
CREATE INDEX idx_auditoria_tabla       ON auditoria(tabla_afectada);
CREATE INDEX idx_auditoria_fecha       ON auditoria(fecha);

-- ============================================================
-- DATOS INICIALES (seed)
-- ============================================================

-- Usuario administrador por defecto
INSERT INTO usuarios (correo, contrasena_hash, nombres, apellidos, rol)
VALUES (
    'admin@gestion.edu.pe',
    '$2b$12$PLACEHOLDER_HASH_REEMPLAZAR_EN_PRODUCCION',
    'Administrador',
    'Sistema',
    'admin'
);

-- FAQs iniciales
INSERT INTO faq (pregunta, respuesta, categoria, orden) VALUES
('¿Cómo recupero mi contraseña?',
 'Haz clic en "¿Olvidaste tu contraseña?" en la pantalla de login e ingresa tu correo institucional. Recibirás un enlace de recuperación en menos de 5 minutos.',
 'Acceso', 1),

('¿Cómo accedo a mis materiales de clase?',
 'Ingresa a "Mis Cursos" desde el dashboard, selecciona el curso y dirígete a la sección "Materiales". Encontrarás archivos PDF, videos y enlaces organizados por el docente.',
 'Cursos', 2),

('¿Puedo ver mi horario en el celular?',
 'Sí, la plataforma es responsiva y funciona en dispositivos móviles. Accede desde tu navegador a la misma URL y encontrarás el módulo "Mi Horario" en el menú principal.',
 'Horario', 3),

('¿Cómo subo un archivo a mi proyecto?',
 'Ingresa al módulo "Proyectos", selecciona tu proyecto, y usa el botón "Adjuntar archivo". Se aceptan formatos PDF, Word, Excel, imágenes y ZIP hasta 50 MB.',
 'Proyectos', 4);

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================