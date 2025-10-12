package org.example.gestionhospitalaria.repository;

import org.example.gestionhospitalaria.model.RecetaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecetaMedicaRepository extends JpaRepository<RecetaMedica, Long> {
}