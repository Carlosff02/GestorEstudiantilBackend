package com.example.demo.service.implementation;

import com.example.demo.persistance.entity.Cursos;
import com.example.demo.persistance.entity.Proyecto;
import com.example.demo.persistance.repository.CursosRepository;
import com.example.demo.persistance.repository.ProyectoRepository;
import com.example.demo.presentation.dtos.GuardarProyectoRequest;
import com.example.demo.presentation.dtos.ProyectoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository repository;
    private final CursosRepository cursosRepository;

    public List<ProyectoResponse> obtenerPorCurso(Long cursoId) {
        return repository.findByCursoIdCurso(cursoId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public ProyectoResponse guardar(GuardarProyectoRequest request) {
        Cursos curso = cursosRepository.findById(request.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(request.getNombre());
        proyecto.setDescripcion(request.getDescripcion());
        proyecto.setFechaInicio(request.getFechaInicio());
        proyecto.setFechaFin(request.getFechaFin());
        proyecto.setEstado(request.getEstado());
        proyecto.setCurso(curso);

        Proyecto guardado = repository.save(proyecto);
        return convertirAResponse(guardado);
    }

    public List<ProyectoResponse> obtenerPorUsuarioId(Long usuarioId){
        return repository.findByUsuarioId(usuarioId);
    }

    public ProyectoResponse actualizar(GuardarProyectoRequest request, Long id) {
        Proyecto proyecto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Cursos curso = cursosRepository.findById(request.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        proyecto.setNombre(request.getNombre());
        proyecto.setDescripcion(request.getDescripcion());
        proyecto.setFechaInicio(request.getFechaInicio());
        proyecto.setFechaFin(request.getFechaFin());
        proyecto.setEstado(request.getEstado());
        proyecto.setCurso(curso);

        Proyecto actualizado = repository.save(proyecto);
        return convertirAResponse(actualizado);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public ProyectoResponse obtenerPorId(Long id) {
        Proyecto proyecto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        return convertirAResponse(proyecto);
    }

    private ProyectoResponse convertirAResponse(Proyecto proyecto) {
        return new ProyectoResponse(
                proyecto.getIdProyecto(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getFechaInicio(),
                proyecto.getFechaFin(),
                proyecto.getEstado(),
                proyecto.getCurso().getIdCurso()
        );
    }
}
