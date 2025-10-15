package org.example.gestionhospitalaria.repository;

import org.example.gestionhospitalaria.model.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // ¡Añade esta importación!

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
    List<Bitacora> findAllByOrderByFechaHoraDesc();
}