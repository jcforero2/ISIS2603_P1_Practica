package co.edu.uniandes.dse.parcial1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstadioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstadioService {

@Autowired
    private EstadioRepository estadioRepository;

    /**
     * Crear estadio validando reglas de negocio
     */
    @Transactional
    public EstadioEntity createEstadio(EstadioEntity estadio) throws IllegalOperationException {
        // Regla 1: ciudad con al menos 3 letras
        if (estadio.getCiudad() == null || estadio.getCiudad().length() < 3) {
            throw new IllegalOperationException("La ciudad debe tener al menos 3 letras");
        }

        // Regla 2: aforo válido
        if (estadio.getAforoMax() <= 1000 || estadio.getAforoMax() >= 1_000_000) {
            throw new IllegalOperationException("El aforo debe estar entre 1000 y 1,000,000");
        }

        // Regla 3: precio > 100,000
        if (estadio.getPrecioAlquiler() <= 100_000) {
            throw new IllegalOperationException("El precio de alquiler debe ser mayor a 100,000");
        }

        return estadioRepository.save(estadio);
    }
}
