package org.example.gestionhospitalaria.service;

import org.example.gestionhospitalaria.model.Paciente;
import java.util.List;
import java.util.Optional;

public interface IPacienteService {

    Paciente registrarNuevoPaciente(Paciente paciente);

    List<Paciente> listarTodosLosPacientes();

    Optional<Paciente> obtenerPacientePorId(Long id);

    Paciente actualizarPaciente(Long id, Paciente pacienteActualizado);

    void desactivarPaciente(Long id);
}