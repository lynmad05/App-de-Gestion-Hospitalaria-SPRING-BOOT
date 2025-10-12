package org.example.gestionhospitalaria.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalles_receta")
public class DetalleReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleReceta;

    @ManyToOne
    @JoinColumn(name = "id_receta", nullable = false)
    private RecetaMedica recetaMedica;

    @Column(length = 100, nullable = false)
    private String medicamento;

    @Column(length = 50)
    private String dosis;

    @Column(length = 50)
    private String frecuencia;

    @Column(length = 50)
    private String duracion;
}