package com.example.demo.presentation.exceptions;

public class CursoNotFoundException extends RuntimeException {
    public CursoNotFoundException(Long id) {
        super("Curso con id "+id+" no encontrado");
    }
}
