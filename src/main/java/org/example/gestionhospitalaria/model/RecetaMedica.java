package org.example.gestionhospitalaria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "recetas_medicas")
public class RecetaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReceta;

    @ManyToOne
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @Column(columnDefinition = "TEXT")
    private String indicaciones;

    @OneToMany(mappedBy = "recetaMedica", cascade = CascadeType.ALL)
    private List<DetalleReceta> detalles;
}