package com.example.demo.persistance.repository;


import com.example.demo.persistance.entity.ProyectoMiembro;
import com.example.demo.presentation.dtos.ProyectoInvitacionResponse;
import com.example.demo.presentation.dtos.ProyectoMiembroResponse;
import com.example.demo.presentation.dtos.ProyectoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProyectoMiembroRepository
        extends JpaRepository<ProyectoMiembro, Long> {

    List<ProyectoMiembro> findByProyectoIdProyectoAndAceptadoTrue(Long proyectoId);

    @Query("""
    SELECT DISTINCT new com.example.demo.presentation.dtos.ProyectoInvitacionResponse(
        p.idProyecto,
        m.idMiembro,
        p.nombre,
        p.descripcion,
        p.fechaInicio,
        p.fechaFin,
        p.estado,
        p.curso.idCurso,
        p.curso.usuario.nombres,
        p.curso.usuario.apellidos,
        m.rol
    )
    FROM Proyecto p
    JOIN p.miembros m
    WHERE m.usuario.idUsuario = :usuarioId
      AND m.aceptado = false
    """)
    List<ProyectoInvitacionResponse> findInvitacionesPendientesByUsuarioId(
            @Param("usuarioId") Long usuarioId
    );

    Optional<ProyectoMiembro> findByProyectoIdProyectoAndUsuarioIdUsuario(Long id, Long idUsuario);
}
