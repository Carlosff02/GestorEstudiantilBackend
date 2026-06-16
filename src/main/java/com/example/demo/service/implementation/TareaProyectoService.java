package com.example.demo.service.implementation;

import com.example.demo.persistance.entity.Proyecto;
import com.example.demo.persistance.entity.TareasProyecto;
import com.example.demo.persistance.entity.Usuario;
import com.example.demo.persistance.repository.ProyectoRepository;
import com.example.demo.persistance.repository.TareaProyectoRepository;
import com.example.demo.persistance.repository.UsuarioRepository;
import com.example.demo.presentation.dtos.ActualizarTareaProyectoRequest;
import com.example.demo.presentation.dtos.GuardarTareaProyectoRequest;
import com.example.demo.presentation.dtos.TareaProyectoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TareaProyectoService {

    private final TareaProyectoRepository repository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<TareaProyectoResponse> obtenerPorProyecto(Long proyectoId) {
        return repository.findByProyectoIdProyecto(proyectoId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public TareaProyectoResponse guardar(GuardarTareaProyectoRequest request) {
        Proyecto proyecto = proyectoRepository.findById(request.getIdProyecto())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Usuario asignadoA = null;
        if (request.getIdAsignadoA() != null) {
            asignadoA = usuarioRepository.findById(request.getIdAsignadoA())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        TareasProyecto tarea = new TareasProyecto();
        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setFechaLimite(request.getFechaLimite());
        tarea.setEstado(request.getEstado());
        tarea.setPrioridad(request.getPrioridad());
        tarea.setFechaCreacion(LocalDateTime.now());
        tarea.setProyecto(proyecto);
        tarea.setAsignadoA(asignadoA);

        TareasProyecto guardada = repository.save(tarea);
        return convertirAResponse(guardada);
    }

    public TareaProyectoResponse actualizar(ActualizarTareaProyectoRequest request) {
        TareasProyecto tarea = repository.findById(request.getIdTarea())
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        Usuario asignadoA = null;
        if (request.getIdAsignadoA() != null) {
            asignadoA = usuarioRepository.findById(request.getIdAsignadoA())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setFechaLimite(request.getFechaLimite());
        tarea.setEstado(request.getEstado());
        tarea.setPrioridad(request.getPrioridad());
        tarea.setAsignadoA(asignadoA);

        TareasProyecto actualizada = repository.save(tarea);
        return convertirAResponse(actualizada);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private TareaProyectoResponse convertirAResponse(TareasProyecto tarea) {
        return new TareaProyectoResponse(
                tarea.getIdTarea(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getFechaLimite(),
                tarea.getEstado(),
                tarea.getPrioridad(),
                tarea.getFechaCreacion(),
                tarea.getProyecto().getIdProyecto(),
                tarea.getAsignadoA() != null ? tarea.getAsignadoA().getIdUsuario() : null
        );
    }
}
