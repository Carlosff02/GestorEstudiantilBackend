package com.example.demo.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuardarProyectoMiembroRequest {

    private String rol;

    private Long idProyecto;

    private String correo;
}
