package com.example.demo.presentation.controllers;

import com.example.demo.persistance.entity.Cursos;
import com.example.demo.presentation.dtos.ActualizarCursoRequest;
import com.example.demo.presentation.dtos.GuardarCursoRequest;
import com.example.demo.service.implementation.CursosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursosController {

    private final CursosService cursosService;

    @GetMapping("/{usuarioId}")
    public List<Cursos> findByUsuarioId(@PathVariable  Long usuarioId){
        return cursosService.findByUsuarioId(usuarioId);
    }

    @PostMapping()
    public Cursos guardarCurso(@RequestBody GuardarCursoRequest request){
        return cursosService.guardarCurso(request);
    }

    @PutMapping()
    public Cursos actualizarCurso(@RequestBody ActualizarCursoRequest request){
        return cursosService.actualizarCurso(request);
    }

    @DeleteMapping("/{cursoId}")
    public void eliminarCurso(@PathVariable Long cursoId){
        cursosService.deleteById(cursoId);
    }

}
