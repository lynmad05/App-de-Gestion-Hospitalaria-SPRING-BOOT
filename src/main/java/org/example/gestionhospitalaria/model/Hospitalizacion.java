package org.example.gestionhospitalaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "hospitalizaciones")
public class Hospitalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHosp;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @OneToOne
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;

    @Column(nullable = false)
    private LocalDate fechaIngreso;

    private LocalDate fechaAlta;

    @Column(columnDefinition = "TEXT")
    private String diagnosticoIngreso;

    @Column(length = 20, nullable = false)
    private String estado; // Activo, Dado de Alta
}