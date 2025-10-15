package org.example.gestionhospitalaria.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.gestionhospitalaria.model.Bitacora;
import org.example.gestionhospitalaria.model.Usuario;
import org.example.gestionhospitalaria.repository.BitacoraRepository;
import org.example.gestionhospitalaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class BitacoraAspect {

    @Autowired
    private BitacoraRepository bitacoraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterReturning("execution(public * org.example.gestionhospitalaria.controller.*.*(..))")
    public void registrarEnBitacora(JoinPoint joinPoint) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails)) {
            return; // Salimos del método para no registrar acciones anónimas o del sistema.
        }

        // Si llegamos aquí, significa que SÍ hay un usuario autenticado.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElse(null);

        // Si el usuario existe en nuestra BD, registramos la acción
        if (usuario != null) {
            String accion = joinPoint.getSignature().getName();

            Bitacora registro = new Bitacora();
            registro.setUsuario(usuario);
            registro.setAccion("Método ejecutado: " + accion);
            registro.setFechaHora(LocalDateTime.now());

            bitacoraRepository.save(registro);
        }
    }
}