package com.example.demo.service.implementation;

import com.example.demo.persistance.entity.Cursos;
import com.example.demo.persistance.entity.Sesion;
import com.example.demo.persistance.repository.CursosRepository;
import com.example.demo.persistance.repository.SesionesRepository;
import com.example.demo.presentation.dtos.ActualizarSesionRequest;
import com.example.demo.presentation.dtos.GuardarSesionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionesService {

    private final SesionesRepository sesionesRepository;
    private final CursosRepository cursosRepository;

    public List<Sesion> findByCursoId(Long cursoId) {
        return sesionesRepository.findByCursoIdCurso(cursoId);
    }

    public Sesion guardarSesion(GuardarSesionRequest request) {
        Cursos curso = cursosRepository.findById(request.cursoId())
                .orElseThrow();

        Sesion sesion = new Sesion();

        sesion.setNombre(request.nombre());
        sesion.setNotas(request.notas());
        sesion.setHoraInicio(request.horaInicio());
        sesion.setHoraFin(request.horaFin());
        sesion.setAula(request.aula());
        sesion.setCurso(curso);
        return sesionesRepository.save(sesion);
    }

    public Sesion actualizarSesion(ActualizarSesionRequest request) {

        Sesion sesion = sesionesRepository.findById(request.id())
                .orElseThrow();

        Cursos curso = cursosRepository.findById(request.cursoId())
                .orElseThrow();

        sesion.setNombre(request.nombre());
        sesion.setNotas(request.notas());
        sesion.setHoraInicio(request.horaInicio());
        sesion.setHoraFin(request.horaFin());
        sesion.setAula(request.aula());
        sesion.setCurso(curso);

        return sesionesRepository.save(sesion);
    }

    public void deleteById(Long sesionId) {
        sesionesRepository.deleteById(sesionId);
    }
}
