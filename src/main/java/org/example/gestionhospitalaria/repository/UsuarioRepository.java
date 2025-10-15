package org.example.gestionhospitalaria.repository;

import org.example.gestionhospitalaria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Data JPA creará la consulta automáticamente a partir del nombre del método
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}