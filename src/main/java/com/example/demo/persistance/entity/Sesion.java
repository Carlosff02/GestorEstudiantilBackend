package com.example.demo.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "sesion")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String notas;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private String aula;
    @ManyToOne
    @JoinColumn(name = "id_curso")
    @JsonIgnore
    private Cursos curso;
}
