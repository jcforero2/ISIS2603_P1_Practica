package co.edu.uniandes.dse.parcial1.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ConciertoDTO {
    private Long id;
    private String nombreArtista;
    private LocalDateTime fecha;
    private int capacidad;
    private double presupuesto;
    private Long estadioId; // opcional: para asociar directamente
}

