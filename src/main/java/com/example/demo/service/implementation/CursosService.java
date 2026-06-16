package com.example.demo.service.implementation;

import com.example.demo.persistance.entity.Cursos;
import com.example.demo.persistance.entity.Usuario;
import com.example.demo.persistance.repository.CursosRepository;
import com.example.demo.persistance.repository.UsuarioRepository;
import com.example.demo.presentation.dtos.ActualizarCursoRequest;
import com.example.demo.presentation.dtos.GuardarCursoRequest;
import com.example.demo.presentation.exceptions.CursoNotFoundException;
import com.example.demo.presentation.exceptions.UsuarioNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CursosService {

    private final CursosRepository repository;
    private final UsuarioRepository usuarioRepository;

    public Cursos guardarCurso(GuardarCursoRequest request){
        Usuario usuario = usuarioRepository.findById(request.usuarioId()).orElseThrow(()->new UsuarioNotFoundException(request.usuarioId()));
        Cursos curso = new Cursos(null,request.nombre(),request.color(),request.description(),request.ciclo(),request.creditos(),request.docente(),true,usuario, new ArrayList<>());
        return repository.save(curso);
    }

    public List<Cursos> findByUsuarioId(Long usuarioId){
        return repository.findByUsuarioIdUsuario(usuarioId);
    }

    public void deleteById(Long cursoId){
        repository.deleteById(cursoId);
    }

    public Cursos actualizarCurso(ActualizarCursoRequest request){
        Cursos curso = repository.findById(request.id()).orElseThrow(()->new CursoNotFoundException(request.id()));
        curso.setActivo(request.activo());
        curso.setNombre(request.nombre());
        curso.setDescripcion(request.description());
        curso.setCiclo(request.ciclo());
        curso.setColor(request.color());
        curso.setDocente(request.docente());
        curso.setCreditos(request.creditos());
        return  repository.save(curso);
    }

}
