package org.example.gestionhospitalaria.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "antecedentes_medicos")
public class AntecedenteMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAntecedente;

    @ManyToOne
    @JoinColumn(name = "id_historia", nullable = false)
    @JsonBackReference
    private HistoriaClinica historiaClinica;

    @Column(length = 50, nullable = false)
    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}