package com.example.demo.persistance.repository;

import com.example.demo.persistance.entity.Usuario;
import com.example.demo.presentation.dtos.UsuarioResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);


    @Query("""
        SELECT new com.example.demo.presentation.dtos.UsuarioResponse(
            u.idUsuario,
            u.correo,
            u.nombres,
            u.apellidos,
            u.rol,
            u.activo,
            u.perfilAccesibilidad,
            u.idioma,
            u.fechaRegistro,
            u.ultimoAcceso
        )
        FROM Usuario u
        """)
    List<UsuarioResponse> listar();

    @Query("""
        SELECT new com.example.demo.presentation.dtos.UsuarioResponse(
            u.idUsuario,
            u.correo,
            u.nombres,
            u.apellidos,
            u.rol,
            u.activo,
            u.perfilAccesibilidad,
            u.idioma,
            u.fechaRegistro,
            u.ultimoAcceso
        )
        FROM Usuario u
        WHERE u.idUsuario = :idUsuario
        """)
    UsuarioResponse obtenerPorId(Long idUsuario);

    boolean existsByCorreo(String correo);
}
