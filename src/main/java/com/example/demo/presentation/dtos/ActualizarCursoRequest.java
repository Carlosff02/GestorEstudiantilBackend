package com.example.demo.presentation.dtos;

public record ActualizarCursoRequest(Long id,String nombre, String docente, String description, String color, String ciclo, Integer creditos, Long usuarioId, Boolean activo) {
}
