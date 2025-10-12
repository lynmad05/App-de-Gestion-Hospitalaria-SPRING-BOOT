package org.example.gestionhospitalaria.repository;

import org.example.gestionhospitalaria.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
}