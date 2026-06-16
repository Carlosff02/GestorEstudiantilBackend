package com.example.demo.persistance.entity;

import jakarta.mail.Session;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "cursos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cursos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;
    private String nombre;
    private String color;
    private String descripcion;
    private String ciclo;
    private Integer creditos;
    private String docente;
    private Boolean activo;
    @ManyToOne()
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @OneToMany(mappedBy = "curso")
    private List<Sesion> sesiones;

}
