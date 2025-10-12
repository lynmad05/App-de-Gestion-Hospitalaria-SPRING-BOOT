package org.example.gestionhospitalaria.controller;

import org.example.gestionhospitalaria.model.*;
import org.example.gestionhospitalaria.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/consultas")
public class ConsultaController {
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private DiagnosticoRepository diagnosticoRepository;
    @Autowired
    private RecetaMedicaRepository recetaMedicaRepository;

    @PostMapping("/iniciar/{idCita}")
    public ResponseEntity<Consulta> iniciarConsulta(@PathVariable Long idCita, @RequestBody Consulta consultaRequest) {
        Cita cita = citaRepository.findById(idCita).orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstado("Atendida");
        citaRepository.save(cita);

        Consulta nuevaConsulta = new Consulta();
        nuevaConsulta.setCita(cita);
        nuevaConsulta.setFechaHoraConsulta(LocalDateTime.now());
        nuevaConsulta.setMotivoConsulta(consultaRequest.getMotivoConsulta());
        nuevaConsulta.setObservaciones(consultaRequest.getObservaciones());

        Consulta consultaGuardada = consultaRepository.save(nuevaConsulta);
        return new ResponseEntity<>(consultaGuardada, HttpStatus.CREATED);
    }

    // 2. Añadir un diagnóstico a una consulta existente
    @PostMapping("/{idConsulta}/diagnosticos")
    public ResponseEntity<Diagnostico> agregarDiagnostico(@PathVariable Long idConsulta, @RequestBody Diagnostico diagnosticoRequest) {
        Consulta consulta = consultaRepository.findById(idConsulta).orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        diagnosticoRequest.setConsulta(consulta);

        // PASO 2: La línea corregida. Usamos el OBJETO (variable en camelCase)
        // para llamar al método .save()
        Diagnostico nuevoDiagnostico = diagnosticoRepository.save(diagnosticoRequest);

        return new ResponseEntity<>(nuevoDiagnostico, HttpStatus.CREATED);
    }

    // 3. Crear una receta para una consulta
    @PostMapping("/{idConsulta}/recetas")
    public ResponseEntity<RecetaMedica> crearReceta(@PathVariable Long idConsulta, @RequestBody RecetaMedica recetaRequest) {
        Consulta consulta = consultaRepository.findById(idConsulta).orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        recetaRequest.setConsulta(consulta);

        if (recetaRequest.getDetalles() != null) {
            recetaRequest.getDetalles().forEach(detalle -> detalle.setRecetaMedica(recetaRequest));
        }

        // PASO 3: La otra línea corregida. Usamos el OBJETO (variable en camelCase).
        RecetaMedica nuevaReceta = recetaMedicaRepository.save(recetaRequest);

        return new ResponseEntity<>(nuevaReceta, HttpStatus.CREATED);
    }
}