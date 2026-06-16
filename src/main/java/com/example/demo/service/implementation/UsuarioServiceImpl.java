package com.example.demo.service.implementation;

import com.example.demo.persistance.entity.Usuario;
import com.example.demo.persistance.repository.UsuarioRepository;
import com.example.demo.presentation.dtos.*;
import com.example.demo.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.persistance.entity.Usuario;
import com.example.demo.persistance.repository.UsuarioRepository;
import com.example.demo.presentation.dtos.ActualizarUsuarioRequest;
import com.example.demo.presentation.dtos.GuardarUsuarioRequest;
import com.example.demo.presentation.dtos.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioResponse> listar() {
        return usuarioRepository.listar();
    }

    @Override
    public UsuarioResponse obtenerPorId(Long idUsuario) {
        return usuarioRepository.obtenerPorId(idUsuario);
    }

    @Override
    public UsuarioResponse guardar(GuardarUsuarioRequest request) {

        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        Usuario usuario = new Usuario();

        usuario.setCorreo(request.correo());
        usuario.setContrasenaHash(
                passwordEncoder.encode(request.contrasena())
        );
        usuario.setNombres(request.nombres());
        usuario.setApellidos(request.apellidos());
        usuario.setRol(request.rol());
        usuario.setActivo(
                request.activo() != null
                        ? request.activo()
                        : true
        );
        usuario.setPerfilAccesibilidad(
                request.perfilAccesibilidad()
        );
        usuario.setIdioma(request.idioma());

        usuario.setFechaRegistro(LocalDateTime.now());

        usuarioRepository.save(usuario);

        return usuarioRepository.obtenerPorId(
                usuario.getIdUsuario()
        );
    }

    @Override
    public UsuarioResponse actualizar(
            Long idUsuario,
            ActualizarUsuarioRequest request
    ) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        usuario.setNombres(request.nombres());
        usuario.setApellidos(request.apellidos());
        usuario.setRol(request.rol());
        usuario.setActivo(request.activo());
        usuario.setPerfilAccesibilidad(
                request.perfilAccesibilidad()
        );
        usuario.setIdioma(request.idioma());

        usuarioRepository.save(usuario);

        return usuarioRepository.obtenerPorId(idUsuario);
    }

    @Override
    public void eliminar(Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        usuarioRepository.delete(usuario);
    }
}
