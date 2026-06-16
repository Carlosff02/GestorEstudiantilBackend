package com.example.demo.presentation.dtos;

public record GuardarCursoRequest(String nombre, String docente, String description, String color, String ciclo, Integer creditos, Long usuarioId) {
}
