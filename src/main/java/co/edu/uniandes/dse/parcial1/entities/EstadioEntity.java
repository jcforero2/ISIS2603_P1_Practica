package co.edu.uniandes.dse.parcial1.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EstadioEntity extends BaseEntity {
    private String ciudad;
    private int aforoMax;
    private double precioAlquiler;

    @OneToMany(mappedBy = "estadio")
    private List<ConciertoEntity> conciertos = new ArrayList<>();
}

