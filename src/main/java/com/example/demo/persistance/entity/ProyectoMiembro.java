package com.example.demo.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "proyecto_miembros",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"id_proyecto", "id_usuario"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoMiembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_miembro")
    private Long idMiembro;

    private String rol;

    private LocalDate fechaAsignacion;

    private Boolean aceptado;

    @ManyToOne
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
