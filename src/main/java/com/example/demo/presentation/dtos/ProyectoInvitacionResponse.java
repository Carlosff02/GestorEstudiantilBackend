package com.example.demo.presentation.dtos;

import java.time.LocalDate;

public record ProyectoInvitacionResponse(Long idProyecto, Long idProyectoMiembro,

       String nombre,

        String descripcion,

        LocalDate fechaInicio,

        LocalDate fechaFin,

        String estado,

        Long idCurso,
                                         String nombreUsuario,
                                         String apellidoUsuario,
                                         String rolPropuesto) {
}
