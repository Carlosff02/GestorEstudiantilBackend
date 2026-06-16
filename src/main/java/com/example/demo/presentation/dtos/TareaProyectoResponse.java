package com.example.demo.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaProyectoResponse {

    private Long idTarea;

    private String titulo;

    private String descripcion;

    private LocalDate fechaLimite;

    private String estado;

    private String prioridad;

    private LocalDateTime fechaCreacion;

    private Long idProyecto;

    private Long idAsignadoA;
}
