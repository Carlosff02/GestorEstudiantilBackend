package com.example.demo.persistance.repository;

import com.example.demo.persistance.entity.TareasProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaProyectoRepository
        extends JpaRepository<TareasProyecto, Long> {

    List<TareasProyecto> findByProyectoIdProyecto(Long proyectoId);
}
