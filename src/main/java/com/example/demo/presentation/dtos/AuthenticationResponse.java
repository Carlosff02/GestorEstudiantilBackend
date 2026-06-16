package com.example.demo.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String token;
    private String role;
    private String nombres;
    private String apellidos;
    private String idioma;
    private String perfilAccesibilidad;


}
