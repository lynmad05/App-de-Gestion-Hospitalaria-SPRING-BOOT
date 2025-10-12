package org.example.gestionhospitalaria.repository;

import org.example.gestionhospitalaria.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleRecetaRepository extends JpaRepository<Consulta, Long> {
}
