package org.example.gestionhospitalaria.controller;

import org.example.gestionhospitalaria.model.Especialidad;
import org.example.gestionhospitalaria.model.Medico;
import org.example.gestionhospitalaria.repository.EspecialidadRepository;
import org.example.gestionhospitalaria.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") // URL base
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    // --- Endpoints para Médicos ---

    @PostMapping("/medicos")
    public ResponseEntity<Medico> crearMedico(@RequestBody Medico medico) {
        medico.setEstado("Activo");
        Medico nuevoMedico = medicoRepository.save(medico);
        return new ResponseEntity<>(nuevoMedico, HttpStatus.CREATED);
    }

    @GetMapping("/medicos")
    public ResponseEntity<List<Medico>> listarMedicos() {
        return new ResponseEntity<>(medicoRepository.findAll(), HttpStatus.OK);
    }

    // --- Endpoints para Especialidades ---

    @PostMapping("/especialidades")
    public ResponseEntity<Especialidad> crearEspecialidad(@RequestBody Especialidad especialidad) {
        Especialidad nuevaEspecialidad = especialidadRepository.save(especialidad);
        return new ResponseEntity<>(nuevaEspecialidad, HttpStatus.CREATED);
    }

    @GetMapping("/especialidades")
    public ResponseEntity<List<Especialidad>> listarEspecialidades() {
        return new ResponseEntity<>(especialidadRepository.findAll(), HttpStatus.OK);
    }

    // --- Endpoint para Asignar Especialidad a Médico ---

    @PostMapping("/medicos/{idMedico}/especialidades/{idEspecialidad}")
    public ResponseEntity<Medico> asignarEspecialidadAMedico(
            @PathVariable Long idMedico,
            @PathVariable Long idEspecialidad) {

        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        medico.getEspecialidades().add(especialidad);
        medicoRepository.save(medico);

        return new ResponseEntity<>(medico, HttpStatus.OK);
    }
}