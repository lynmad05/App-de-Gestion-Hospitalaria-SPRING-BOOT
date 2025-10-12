package org.example.gestionhospitalaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id // Marca este campo como la Llave Primaria (PK).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @Column(length = 8, nullable = false, unique = true)
    private String dni;

    @Column(length = 50, nullable = false)
    private String nombres;

    @Column(length = 50, nullable = false)
    private String apellidos;

    private LocalDate fechaNacimiento;

    @Column(length = 10)
    private String sexo;

    @Column(length = 100)
    private String direccion;

    @Column(length = 15)
    private String telefono;

    @Column(length = 50, unique = true)
    private String correo;

    @Column(length = 10, nullable = false)
    private String estado;



    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL)
    private HistoriaClinica historiaClinica;
}