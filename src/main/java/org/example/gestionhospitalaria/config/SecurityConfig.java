package org.example.gestionhospitalaria.config;

import org.example.gestionhospitalaria.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            var userEntity = usuarioRepository.findByNombreUsuario(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            return User.withUsername(userEntity.getNombreUsuario())
                    .password(userEntity.getContrasena())
                    .roles(userEntity.getRol().toUpperCase())
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para APIs REST
                .authorizeHttpRequests(auth -> auth
                        // 1. RUTAS PÚBLICAS (No requieren login)
                        .requestMatchers("/", "/index.html", "/js/**", "/css/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll() // Login
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll() // Creación de usuario (para registrar el primero)

                        // 2. RUTAS PROTEGIDAS POR ROL (Requieren login y un rol específico)

                        // Gestión de Pacientes
                        .requestMatchers(HttpMethod.POST, "/api/v1/pacientes").hasAnyRole("ADMIN", "RECEPCIONISTA")

                        // Gestión de Citas (¡AQUÍ ESTÁ LA ACTUALIZACIÓN!)
                        .requestMatchers(HttpMethod.POST, "/api/v1/citas").hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/citas/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO") // <-- ¡NUEVA LÍNEA!

                        // Gestión de Consultas
                        .requestMatchers("/api/v1/consultas/**").hasRole("MEDICO")

                        // Administración y Seguridad
                        .requestMatchers("/api/v1/bitacora/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN") // (GET, PUT, DELETE de usuarios)

                        // 3. RUTAS AUTENTICADAS (Cualquier otra ruta requiere solo estar logueado)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Usa sesiones sin estado (JWT)
                .authenticationProvider(authenticationProvider) // Define el proveedor de autenticación
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Añade el filtro JWT

        return http.build();
    }
}