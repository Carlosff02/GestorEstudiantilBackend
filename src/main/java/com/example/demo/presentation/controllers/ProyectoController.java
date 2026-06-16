package com.example.demo.presentation.controllers;

import com.example.demo.service.implementation.ProyectoService;
import com.example.demo.presentation.dtos.GuardarProyectoRequest;
import com.example.demo.presentation.dtos.ProyectoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    @GetMapping("/curso/{cursoId}")
    public List<ProyectoResponse> obtenerPorCurso(
            @PathVariable Long cursoId) {

        return proyectoService.obtenerPorCurso(cursoId);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<ProyectoResponse> obtenerPorUsuarioId(@PathVariable Long usuarioId){
        return proyectoService.obtenerPorUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    public ProyectoResponse obtenerPorId(
            @PathVariable Long id) {

        return proyectoService.obtenerPorId(id);
    }

    @PostMapping
    public ProyectoResponse guardar(
            @RequestBody GuardarProyectoRequest request) {

        return proyectoService.guardar(request);
    }

    @PutMapping("/{id}")
    public ProyectoResponse actualizar(
            @PathVariable Long id,
            @RequestBody GuardarProyectoRequest request) {

        return proyectoService.actualizar(request, id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        proyectoService.eliminar(id);
    }
}
