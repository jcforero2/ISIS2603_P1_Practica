package co.edu.uniandes.dse.parcial1.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.ConciertoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConciertoService {

@Autowired
    private ConciertoRepository conciertoRepository;

    @Transactional
    public ConciertoEntity createConcierto(ConciertoEntity concierto) throws IllegalOperationException {
        // Regla 1: fecha no puede estar en el pasado
        if (concierto.getFecha().isBefore(LocalDateTime.now())) {
            throw new IllegalOperationException("La fecha no puede estar en el pasado");
        }

        // Regla 2: capacidad > 10
        if (concierto.getCapacidad() <= 10) {
            throw new IllegalOperationException("La capacidad debe ser mayor a 10");
        }

        // Regla 3: presupuesto > 1000
        if (concierto.getPresupuesto() <= 1000) {
            throw new IllegalOperationException("El presupuesto debe ser mayor a 1000");
        }

        return conciertoRepository.save(concierto);
    }

}
