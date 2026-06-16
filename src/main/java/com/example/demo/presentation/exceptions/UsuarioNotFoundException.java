package com.example.demo.presentation.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long usuarioId) {
        super("El usuario con id "+usuarioId+" no existe");
    }
}
