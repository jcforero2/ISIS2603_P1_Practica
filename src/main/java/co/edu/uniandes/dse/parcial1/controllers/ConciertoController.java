package co.edu.uniandes.dse.parcial1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.parcial1.dto.ConciertoDTO;
import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.services.ConciertoService;
import co.edu.uniandes.dse.parcial1.services.ConciertoEstadioService;

import java.net.URI;

@RestController
@RequestMapping("/conciertos")
public class ConciertoController {

    @Autowired
    private ConciertoService conciertoService;

    @Autowired
    private ConciertoEstadioService conciertoEstadioService;

    @PostMapping
    public ResponseEntity<ConciertoDTO> createConcierto(@RequestBody ConciertoDTO dto) throws IllegalOperationException {
        // --- convertir DTO -> Entity ---
        ConciertoEntity entity = new ConciertoEntity();
        entity.setNombreArtista(dto.getNombreArtista());
        entity.setFecha(dto.getFecha());
        entity.setCapacidad(dto.getCapacidad());
        entity.setPresupuesto(dto.getPresupuesto());

        // Crear concierto con validaciones
        ConciertoEntity creado = conciertoService.createConcierto(entity);

        // Si llega un estadioId, asociar
        if (dto.getEstadioId() != null) {
            creado = conciertoEstadioService.assignEstadio(creado.getId(), dto.getEstadioId());
        }

        // --- convertir Entity -> DTO ---
        ConciertoDTO response = new ConciertoDTO();
        response.setId(creado.getId());
        response.setNombreArtista(creado.getNombreArtista());
        response.setFecha(creado.getFecha());
        response.setCapacidad(creado.getCapacidad());
        response.setPresupuesto(creado.getPresupuesto());
        if (creado.getEstadio() != null) {
            response.setEstadioId(creado.getEstadio().getId());
        }

        return ResponseEntity
                .created(URI.create("/conciertos/" + response.getId()))
                .body(response);
    }
}


