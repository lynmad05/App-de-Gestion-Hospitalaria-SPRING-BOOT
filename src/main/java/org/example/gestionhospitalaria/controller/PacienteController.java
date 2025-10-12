package org.example.gestionhospitalaria.controller;

import org.example.gestionhospitalaria.model.Paciente;
import org.example.gestionhospitalaria.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Anotación clave: Combina @Controller y @ResponseBody. Convierte la clase en un controlador REST.
@RequestMapping("/api/v1/pacientes") // Define la URL base para todos los endpoints de este controlador.
public class PacienteController {

    @Autowired // Inyectamos nuestro servicio para poder usar sus métodos.
    private IPacienteService pacienteService;

    // Endpoint para CREAR un nuevo paciente
    // Se activa con una petición POST a /api/v1/pacientes
    @PostMapping
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        Paciente nuevoPaciente = pacienteService.registrarNuevoPaciente(paciente);
        return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED); // Devuelve el paciente creado y el estado 201 (CREATED)
    }

    // Endpoint para LISTAR todos los pacientes
    // Se activa con una petición GET a /api/v1/pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodosLosPacientes();
        return new ResponseEntity<>(pacientes, HttpStatus.OK); // Devuelve la lista y el estado 200 (OK)
    }

    // Endpoint para OBTENER un paciente por su ID
    // Se activa con una petición GET a /api/v1/pacientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPacientePorId(@PathVariable("id") Long id) {
        // .map() se ejecuta si el paciente existe, .orElseThrow() si no.
        Paciente paciente = pacienteService.obtenerPacientePorId(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el ID: " + id));
        return new ResponseEntity<>(paciente, HttpStatus.OK);
    }

    // Endpoint para ACTUALIZAR un paciente existente
    // Se activa con una petición PUT a /api/v1/pacientes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable("id") Long id, @RequestBody Paciente pacienteActualizado) {
        Paciente paciente = pacienteService.actualizarPaciente(id, pacienteActualizado);
        return new ResponseEntity<>(paciente, HttpStatus.OK);
    }

    // Endpoint para DESACTIVAR (borrado lógico) un paciente
    // Se activa con una petición DELETE a /api/v1/pacientes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarPaciente(@PathVariable("id") Long id) {
        pacienteService.desactivarPaciente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve el estado 204 (NO CONTENT)
    }
}