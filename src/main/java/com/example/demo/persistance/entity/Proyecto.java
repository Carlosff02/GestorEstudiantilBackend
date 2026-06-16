package com.example.demo.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "proyectos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long idProyecto;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Cursos curso;


    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<ProyectoMiembro> miembros;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<TareasProyecto> tareas;
}