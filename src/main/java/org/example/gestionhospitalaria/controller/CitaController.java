package org.example.gestionhospitalaria.controller;

import org.example.gestionhospitalaria.model.Cita;
import org.example.gestionhospitalaria.model.Medico;
import org.example.gestionhospitalaria.model.Paciente;
import org.example.gestionhospitalaria.repository.CitaRepository;
import org.example.gestionhospitalaria.repository.MedicoRepository;
import org.example.gestionhospitalaria.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<Cita> agendarCita(@RequestBody Cita citaRequest) {
        // Buscamos el paciente y el médico por sus IDs
        Paciente paciente = pacienteRepository.findById(citaRequest.getPaciente().getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoRepository.findById(citaRequest.getMedico().getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // Creamos la nueva cita
        Cita nuevaCita = new Cita();
        nuevaCita.setPaciente(paciente);
        nuevaCita.setMedico(medico);
        nuevaCita.setFecha(citaRequest.getFecha());
        nuevaCita.setHora(citaRequest.getHora());
        nuevaCita.setMotivo(citaRequest.getMotivo());
        nuevaCita.setEstado("Programada"); // Estado inicial

        Cita citaGuardada = citaRepository.save(nuevaCita);
        return new ResponseEntity<>(citaGuardada, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cita>> listarTodasLasCitas() {
        List<Cita> citas = citaRepository.findAll();
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }
}