package com.example.demo.presentation.dtos;


public record GuardarUsuarioRequest(
        String correo,
        String contrasena,
        String nombres,
        String apellidos,
        String rol,
        Boolean activo,
        String perfilAccesibilidad,
        String idioma
) {
}
