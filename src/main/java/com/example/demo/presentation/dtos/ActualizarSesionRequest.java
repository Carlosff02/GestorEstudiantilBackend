package com.example.demo.presentation.dtos;

import java.time.LocalDateTime;

public record ActualizarSesionRequest(
        Long id,
        String nombre,
        String notas,
        LocalDateTime horaInicio,
        LocalDateTime horaFin,
        String aula,
        Long cursoId
) {
}
