package com.example.demo.presentation.dtos;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long idUsuario,
        String correo,
        String nombres,
        String apellidos,
        String rol,
        Boolean activo,
        String perfilAccesibilidad,
        String idioma,
        LocalDateTime fechaRegistro,
        LocalDateTime ultimoAcceso
) {
}
