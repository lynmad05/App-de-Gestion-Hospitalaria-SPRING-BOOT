package org.example.gestionhospitalaria.controller;

import org.example.gestionhospitalaria.model.Bitacora;
import org.example.gestionhospitalaria.repository.BitacoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bitacora")
public class BitacoraController {

    @Autowired
    private BitacoraRepository bitacoraRepository;

    @GetMapping
    public ResponseEntity<List<Bitacora>> listarBitacora() {
        // Ordenamos por fecha descendente para ver lo más reciente primero
        List<Bitacora> registros = bitacoraRepository.findAllByOrderByFechaHoraDesc();
        return ResponseEntity.ok(registros);
    }
}