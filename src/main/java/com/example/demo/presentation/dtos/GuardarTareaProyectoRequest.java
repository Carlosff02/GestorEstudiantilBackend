package com.example.demo.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuardarTareaProyectoRequest {

    private String titulo;

    private String descripcion;

    private LocalDate fechaLimite;

    private String estado;

    private String prioridad;

    private Long idProyecto;

    private Long idAsignadoA;
}
