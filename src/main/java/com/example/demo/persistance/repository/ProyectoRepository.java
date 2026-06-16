package com.example.demo.persistance.repository;


import com.example.demo.persistance.entity.Proyecto;
import com.example.demo.presentation.dtos.ProyectoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProyectoRepository
        extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByCursoIdCurso(Long idCurso);

    @Query("""
    SELECT DISTINCT new com.example.demo.presentation.dtos.ProyectoResponse(
        p.idProyecto,
        p.nombre,
        p.descripcion,
        p.fechaInicio,
        p.fechaFin,
        p.estado,
        p.curso.idCurso
    )
    FROM Proyecto p
    LEFT JOIN p.miembros m
    WHERE p.curso.usuario.idUsuario = :usuarioId
       OR (
            m.usuario.idUsuario = :usuarioId
            AND m.aceptado = true
          )
    """)
    List<ProyectoResponse> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
