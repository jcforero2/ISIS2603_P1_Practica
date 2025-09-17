package co.edu.uniandes.dse.parcial1.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ConciertoEntity extends BaseEntity {
    private String nombreArtista;
    private LocalDateTime fecha;
    private int capacidad;
    private double presupuesto;

    @ManyToOne
    private EstadioEntity estadio;
}

