package org.example.gestionhospitalaria.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "diagnosticos")
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDiagnostico;

    @ManyToOne
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(length = 20, nullable = false)
    private String tipo; // Presuntivo o Definitivo
}