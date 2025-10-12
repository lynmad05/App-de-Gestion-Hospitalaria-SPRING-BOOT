package org.example.gestionhospitalaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedico;

    @Column(length = 100, nullable = false)
    private String nombres;

    @Column(length = 100, nullable = false)
    private String apellidos;

    @Column(length = 20, nullable = false, unique = true)
    private String colegiatura;

    @Column(length = 15)
    private String telefono;

    @Column(length = 50, unique = true)
    private String correo;

    @Column(length = 10, nullable = false)
    private String estado;

    // --- RELACIÓN MUCHOS A MUCHOS ---
    @ManyToMany
    @JoinTable(
            name = "medico_especialidad",
            joinColumns = @JoinColumn(name = "id_medico"),
            inverseJoinColumns = @JoinColumn(name = "id_especialidad")
    )
    private Set<Especialidad> especialidades = new HashSet<>();
}