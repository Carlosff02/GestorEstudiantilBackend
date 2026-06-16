package com.example.demo.presentation.controllers;

import com.example.demo.persistance.entity.Sesion;
import com.example.demo.presentation.dtos.ActualizarSesionRequest;
import com.example.demo.presentation.dtos.GuardarSesionRequest;
import com.example.demo.service.implementation.SesionesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
@RequiredArgsConstructor
public class SesionController {

    private final SesionesService sesionesService;

    @GetMapping("/{cursoId}")
    public List<Sesion> findByCursoId(@PathVariable Long cursoId) {
        return sesionesService.findByCursoId(cursoId);
    }

    @PostMapping
    public Sesion guardarSesion(
            @RequestBody GuardarSesionRequest request
    ) {
        return sesionesService.guardarSesion(request);
    }

    @PutMapping
    public Sesion actualizarSesion(
            @RequestBody ActualizarSesionRequest request
    ) {
        return sesionesService.actualizarSesion(request);
    }

    @DeleteMapping("/{sesionId}")
    public void eliminarSesion(
            @PathVariable Long sesionId
    ) {
        sesionesService.deleteById(sesionId);
    }
}
