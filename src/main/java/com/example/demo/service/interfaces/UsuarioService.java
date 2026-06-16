package com.example.demo.service.interfaces;

import com.example.demo.presentation.dtos.*;

import java.util.List;

public interface UsuarioService {

    List<UsuarioResponse> listar();

    UsuarioResponse obtenerPorId(Long idUsuario);

    UsuarioResponse guardar(GuardarUsuarioRequest request);

    UsuarioResponse actualizar(
            Long idUsuario,
            ActualizarUsuarioRequest request
    );

    void eliminar(Long idUsuario);
}
