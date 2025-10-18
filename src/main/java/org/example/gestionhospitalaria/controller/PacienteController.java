package org.example.gestionhospitalaria.controller;

import org.example.gestionhospitalaria.model.Paciente;
import org.example.gestionhospitalaria.service.IPacienteService; // Assuming IPacienteService exists
// Or use PacienteService directly if IPacienteService doesn't exist
// import org.example.gestionhospitalaria.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

    @Autowired
    private IPacienteService pacienteService; // Or PacienteService

    @PostMapping
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        Paciente nuevoPaciente = pacienteService.registrarNuevoPaciente(paciente);
        return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodosLosPacientes();
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPacientePorId(@PathVariable("id") Long id) {
        Paciente paciente = pacienteService.obtenerPacientePorId(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el ID: " + id));
        return new ResponseEntity<>(paciente, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable("id") Long id, @RequestBody Paciente pacienteActualizado) {
        Paciente paciente = pacienteService.actualizarPaciente(id, pacienteActualizado);
        return new ResponseEntity<>(paciente, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarPaciente(@PathVariable("id") Long id) {
        pacienteService.desactivarPaciente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve el estado 204 (NO CONTENT)
    }

    // --- ONLY ONE buscarPacientes method ---
    @GetMapping("/buscar")
    public ResponseEntity<List<Paciente>> buscarPacientes(@RequestParam String termino) {
        // Assuming your service has a method named buscarPacientes or buscarPorDniONombre
        List<Paciente> pacientes = pacienteService.buscarPacientes(termino);
        return ResponseEntity.ok(pacientes);
    }
}