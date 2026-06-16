package com.example.demo.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tareas_proyecto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareasProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long idTarea;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDate fechaLimite;

    private String estado;

    private String prioridad;

    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "asignado_a")
    private Usuario asignadoA;
}
