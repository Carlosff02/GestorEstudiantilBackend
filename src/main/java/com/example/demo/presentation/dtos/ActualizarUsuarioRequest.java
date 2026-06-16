package com.example.demo.presentation.dtos;

public record ActualizarUsuarioRequest(
        String nombres,
        String apellidos,
        String rol,
        Boolean activo,
        String perfilAccesibilidad,
        String idioma
) {
}
