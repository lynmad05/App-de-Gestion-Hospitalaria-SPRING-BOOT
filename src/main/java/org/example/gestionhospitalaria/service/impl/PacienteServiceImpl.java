package org.example.gestionhospitalaria.service.impl;

import org.example.gestionhospitalaria.model.HistoriaClinica;
import org.example.gestionhospitalaria.model.Paciente;
import org.example.gestionhospitalaria.repository.PacienteRepository;
import org.example.gestionhospitalaria.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements IPacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public Paciente registrarNuevoPaciente(Paciente paciente) {
        HistoriaClinica historia = new HistoriaClinica();
        historia.setFechaApertura(LocalDate.now());
        historia.setObservaciones("Historia clínica creada al registrar el paciente.");
        historia.setPaciente(paciente);

        paciente.setHistoriaClinica(historia);
        paciente.setEstado("Activo");

        return pacienteRepository.save(paciente);
    }

    @Override
    public List<Paciente> listarTodosLosPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> obtenerPacientePorId(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Paciente actualizarPaciente(Long id, Paciente pacienteActualizado) {
        return pacienteRepository.findById(id).map(pacienteExistente -> {
            pacienteExistente.setNombres(pacienteActualizado.getNombres());
            pacienteExistente.setApellidos(pacienteActualizado.getApellidos());
            pacienteExistente.setDni(pacienteActualizado.getDni());
            pacienteExistente.setFechaNacimiento(pacienteActualizado.getFechaNacimiento());
            pacienteExistente.setDireccion(pacienteActualizado.getDireccion());
            pacienteExistente.setTelefono(pacienteActualizado.getTelefono());
            pacienteExistente.setCorreo(pacienteActualizado.getCorreo());
            return pacienteRepository.save(pacienteExistente);
        }).orElseThrow(() -> new RuntimeException("Paciente no encontrado con el ID: " + id));
    }

    @Override
    public void desactivarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el ID: " + id));
        paciente.setEstado("Inactivo");
        pacienteRepository.save(paciente);
    }
}