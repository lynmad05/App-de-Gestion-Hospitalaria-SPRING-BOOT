package org.example.gestionhospitalaria.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalles_factura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleFactura;

    @ManyToOne
    @JoinColumn(name = "id_factura", nullable = false)
    @JsonIgnore
    private Factura factura;

    @Column(length = 100, nullable = false)
    private String concepto; // Consulta, Medicamento, Procedimiento

    @Column(nullable = false)
    private Double monto;
}