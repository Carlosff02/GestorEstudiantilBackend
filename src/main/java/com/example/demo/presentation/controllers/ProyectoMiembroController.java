package com.example.demo.presentation.controllers;

import com.example.demo.presentation.dtos.ProyectoInvitacionResponse;
import com.example.demo.presentation.dtos.ProyectoResponse;
import com.example.demo.service.implementation.ProyectoMiembroService;
import com.example.demo.presentation.dtos.GuardarProyectoMiembroRequest;
import com.example.demo.presentation.dtos.ProyectoMiembroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyecto-miembros")
@RequiredArgsConstructor
public class ProyectoMiembroController {

    private final ProyectoMiembroService miembroService;

    @GetMapping("/{proyectoId}")
    public List<ProyectoMiembroResponse> obtenerPorProyecto(
            @PathVariable Long proyectoId) {

        return miembroService.obtenerPorProyecto(proyectoId);
    }

    @GetMapping("/invitaciones/{usuarioId}")
    public List<ProyectoInvitacionResponse> obtenerInvitaciones(@PathVariable Long usuarioId){
        return miembroService.obtenerInvitaciones(usuarioId);
    }

    @GetMapping("/aceptar/{id}/{idUsuario}")
    public ProyectoMiembroResponse aceptarSolicitud(@PathVariable Long id, @PathVariable Long idUsuario){
        return miembroService.aceptar(id, idUsuario);
    }

    @PostMapping
    public ProyectoMiembroResponse guardar(
            @RequestBody GuardarProyectoMiembroRequest request) {

        return miembroService.guardar(request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        miembroService.eliminar(id);
    }
}
