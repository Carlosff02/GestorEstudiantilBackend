CREATE TABLE sesion(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(125) NOT NULL,
    notas TEXT,
    hora_inicio TIMESTAMP,
    hora_fin TIMESTAMP,
    aula VARCHAR(75),
    curso_id INTEGER REFERENCES cursos(id_curso)
)
