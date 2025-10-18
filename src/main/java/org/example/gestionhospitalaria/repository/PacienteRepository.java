package org.example.gestionhospitalaria.repository;

import org.example.gestionhospitalaria.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByDniContainingIgnoreCaseOrNombresContainingIgnoreCase(String dni, String nombres);
}
