package com.example.demo.persistance.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String correo;
    private String contrasenaHash;
    private String nombres;
    private String apellidos;
    private String rol;
    private Boolean activo;
    private String perfilAccesibilidad;
    private String idioma;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return contrasenaHash;
    }

    @Override
    public String getUsername() {
        return correo;
    }
}

