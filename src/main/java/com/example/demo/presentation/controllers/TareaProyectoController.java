package com.example.demo.presentation.controllers;

import com.example.demo.service.implementation.TareaProyectoService;
import com.example.demo.presentation.dtos.ActualizarTareaProyectoRequest;
import com.example.demo.presentation.dtos.GuardarTareaProyectoRequest;
import com.example.demo.presentation.dtos.TareaProyectoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaProyectoController {

    private final TareaProyectoService tareaService;

    @GetMapping("/{proyectoId}")
    public List<TareaProyectoResponse> obtenerPorProyecto(
            @PathVariable Long proyectoId) {

        return tareaService.obtenerPorProyecto(proyectoId);
    }

    @PostMapping
    public TareaProyectoResponse guardar(
            @RequestBody GuardarTareaProyectoRequest request) {

        return tareaService.guardar(request);
    }

    @PutMapping
    public TareaProyectoResponse actualizar(
            @RequestBody ActualizarTareaProyectoRequest request) {

        return tareaService.actualizar(request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        tareaService.eliminar(id);
    }
}

