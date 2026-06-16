package com.example.demo.persistance.repository;

import com.example.demo.persistance.entity.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursosRepository extends JpaRepository<Cursos, Long> {

    List<Cursos> findByUsuarioIdUsuario(Long usuarioId);
}
