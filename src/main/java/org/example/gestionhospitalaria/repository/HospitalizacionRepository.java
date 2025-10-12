package org.example.gestionhospitalaria.repository;

import org.example.gestionhospitalaria.model.Hospitalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalizacionRepository extends JpaRepository<Hospitalizacion, Long> {
}