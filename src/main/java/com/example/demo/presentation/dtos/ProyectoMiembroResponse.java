package com.example.demo.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoMiembroResponse {

    private Long idMiembro;

    private String rol;

    private LocalDate fechaAsignacion;

    private Long idProyecto;

    private Long idUsuario;
    private String nombres;
    private String apellidos;
}
