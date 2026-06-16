package com.example.demo.persistance.repository;

import com.example.demo.persistance.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SesionesRepository extends JpaRepository<Sesion, Long> {
    List<Sesion> findByCursoIdCurso(Long cursoId);
}
