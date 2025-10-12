package org.example.gestionhospitalaria.controller;

import org.example.gestionhospitalaria.model.Habitacion;
import org.example.gestionhospitalaria.model.Hospitalizacion;
import org.example.gestionhospitalaria.model.Paciente;
import org.example.gestionhospitalaria.repository.HabitacionRepository;
import org.example.gestionhospitalaria.repository.HospitalizacionRepository;
import org.example.gestionhospitalaria.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HospitalizacionController {

    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private HospitalizacionRepository hospitalizacionRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    // --- Endpoints para Habitaciones ---
    @PostMapping("/habitaciones")
    public ResponseEntity<Habitacion> crearHabitacion(@RequestBody Habitacion habitacion) {
        habitacion.setEstado("Disponible");
        Habitacion nuevaHabitacion = habitacionRepository.save(habitacion);
        return new ResponseEntity<>(nuevaHabitacion, HttpStatus.CREATED);
    }

    @GetMapping("/habitaciones")
    public ResponseEntity<List<Habitacion>> listarHabitaciones() {
        return new ResponseEntity<>(habitacionRepository.findAll(), HttpStatus.OK);
    }

    // --- Endpoints para Hospitalización ---
    @PostMapping("/hospitalizaciones")
    public ResponseEntity<Hospitalizacion> registrarIngreso(@RequestBody Hospitalizacion hospRequest) {
        Paciente paciente = pacienteRepository.findById(hospRequest.getPaciente().getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        Habitacion habitacion = habitacionRepository.findById(hospRequest.getHabitacion().getIdHabitacion())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        if (!"Disponible".equals(habitacion.getEstado())) {
            throw new RuntimeException("La habitación no está disponible");
        }

        // Actualizar el estado de la habitación a "Ocupada"
        habitacion.setEstado("Ocupada");
        habitacionRepository.save(habitacion);

        // Crear el registro de hospitalización
        Hospitalizacion nuevaHosp = new Hospitalizacion();
        nuevaHosp.setPaciente(paciente);
        nuevaHosp.setHabitacion(habitacion);
        nuevaHosp.setFechaIngreso(LocalDate.now());
        nuevaHosp.setDiagnosticoIngreso(hospRequest.getDiagnosticoIngreso());
        nuevaHosp.setEstado("Activo");

        Hospitalizacion hospGuardada = hospitalizacionRepository.save(nuevaHosp);
        return new ResponseEntity<>(hospGuardada, HttpStatus.CREATED);
    }

    @PutMapping("/hospitalizaciones/{idHosp}/alta")
    public ResponseEntity<Hospitalizacion> darDeAlta(@PathVariable Long idHosp) {
        Hospitalizacion hospitalizacion = hospitalizacionRepository.findById(idHosp)
                .orElseThrow(() -> new RuntimeException("Registro de hospitalización no encontrado"));

        Habitacion habitacion = hospitalizacion.getHabitacion();
        habitacion.setEstado("Disponible"); // La habitación vuelve a estar disponible
        habitacionRepository.save(habitacion);

        hospitalizacion.setFechaAlta(LocalDate.now());
        hospitalizacion.setEstado("Dado de Alta");
        Hospitalizacion hospActualizada = hospitalizacionRepository.save(hospitalizacion);

        return new ResponseEntity<>(hospActualizada, HttpStatus.OK);
    }

    @GetMapping("/hospitalizaciones")
    public ResponseEntity<List<Hospitalizacion>> listarHospitalizaciones() {
        return new ResponseEntity<>(hospitalizacionRepository.findAll(), HttpStatus.OK);
    }
}