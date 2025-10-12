package org.example.gestionhospitalaria.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(length = 50, nullable = false, unique = true)
    private String nombreUsuario;

    @Column(length = 100, nullable = false)
    private String contrasena;

    @Column(length = 20, nullable = false)
    private String rol;
}