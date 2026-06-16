package com.example.demo.presentation.controllers;

import com.example.demo.presentation.dtos.*;
import com.example.demo.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtenerPorId(
            @PathVariable Long id
    ) {
        return usuarioService.obtenerPorId(id);
    }

    @PostMapping
    public UsuarioResponse crear(
            @RequestBody GuardarUsuarioRequest request
    ) {
        return usuarioService.guardar(request);
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarUsuarioRequest request
    ) {
        return usuarioService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id
    ) {
        usuarioService.eliminar(id);
    }
}
