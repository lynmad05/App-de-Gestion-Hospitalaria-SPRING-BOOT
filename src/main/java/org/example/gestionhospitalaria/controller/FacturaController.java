package org.example.gestionhospitalaria.controller;

import org.example.gestionhospitalaria.model.DetalleFactura;
import org.example.gestionhospitalaria.model.Factura;
import org.example.gestionhospitalaria.model.Paciente;
import org.example.gestionhospitalaria.repository.FacturaRepository;
import org.example.gestionhospitalaria.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    public ResponseEntity<Factura> generarFactura(@RequestBody Factura facturaRequest) {
        Paciente paciente = pacienteRepository.findById(facturaRequest.getPaciente().getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Factura nuevaFactura = new Factura();
        nuevaFactura.setPaciente(paciente);
        nuevaFactura.setFechaEmision(LocalDate.now());
        nuevaFactura.setEstado("Pendiente");

        // Asignamos la factura a cada detalle y calculamos el total
        double totalFactura = 0.0;
        if (facturaRequest.getDetalles() != null) {
            for(DetalleFactura detalle : facturaRequest.getDetalles()) {
                detalle.setFactura(nuevaFactura); // Importante para la relación
                totalFactura += detalle.getMonto();
            }
        }

        nuevaFactura.setTotal(totalFactura);
        nuevaFactura.setDetalles(facturaRequest.getDetalles());

        Factura facturaGuardada = facturaRepository.save(nuevaFactura);
        return new ResponseEntity<>(facturaGuardada, HttpStatus.CREATED);
    }

    @PutMapping("/{idFactura}/pagar")
    public ResponseEntity<Factura> pagarFactura(@PathVariable Long idFactura) {
        Factura factura = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        factura.setEstado("Pagado");
        Factura facturaPagada = facturaRepository.save(factura);
        return new ResponseEntity<>(facturaPagada, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Factura>> listarFacturas() {
        return new ResponseEntity<>(facturaRepository.findAll(), HttpStatus.OK);
    }
}