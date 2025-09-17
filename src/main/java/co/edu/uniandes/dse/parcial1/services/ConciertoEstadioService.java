package co.edu.uniandes.dse.parcial1.services;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.ConciertoRepository;
import co.edu.uniandes.dse.parcial1.repositories.EstadioRepository;

@Slf4j
@Service
public class ConciertoEstadioService {

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Autowired
    private EstadioRepository estadioRepository;

    /**
     * Asocia un estadio existente a un concierto existente
     */
    @Transactional(rollbackFor = IllegalOperationException.class)
    public ConciertoEntity assignEstadio(Long conciertoId, Long estadioId) throws IllegalOperationException {
        log.info("Inicia proceso de asociar concierto {} con estadio {}", conciertoId, estadioId);

        ConciertoEntity concierto = conciertoRepository.findById(conciertoId)
                .orElseThrow(() -> new IllegalOperationException("El concierto con id " + conciertoId + " no existe"));

        EstadioEntity estadio = estadioRepository.findById(estadioId)
                .orElseThrow(() -> new IllegalOperationException("El estadio con id " + estadioId + " no existe"));

        // Recargar estadio con conciertos actualizados (garantiza que la lista no esté vacía)
        EstadioEntity estadioRef = estadioRepository.findById(estadio.getId())
                .orElseThrow(() -> new IllegalOperationException("El estadio no existe"));

        // Validar reglas de negocio
        validateAssociation(concierto, estadioRef);

        concierto.setEstadio(estadio);
        conciertoRepository.save(concierto);

        log.info("Termina proceso de asociar concierto {} con estadio {}", conciertoId, estadioId);
        return concierto;
    }

    /**
     * Obtiene el estadio asociado a un concierto
     */
    @Transactional
    public EstadioEntity getEstadio(Long conciertoId) throws IllegalOperationException {
        ConciertoEntity concierto = conciertoRepository.findById(conciertoId)
                .orElseThrow(() -> new IllegalOperationException("El concierto con id " + conciertoId + " no existe"));

        return concierto.getEstadio();
    }

    /**
     * Validaciones de reglas de negocio
     */
    private void validateAssociation(ConciertoEntity concierto, EstadioEntity estadio) throws IllegalOperationException {
        // Capacidad
        if (concierto.getCapacidad() > estadio.getAforoMax()) {
            throw new IllegalOperationException("La capacidad del concierto excede el aforo del estadio");
        }

        // Presupuesto
        if (estadio.getPrecioAlquiler() > concierto.getPresupuesto()) {
            throw new IllegalOperationException("El presupuesto del concierto no alcanza para alquilar el estadio");
        }

        // Diferencia mínima de 2 días entre conciertos
        for (ConciertoEntity existente : estadio.getConciertos()) {
            if (existente.getId().equals(concierto.getId())) continue; // evitar compararse consigo mismo

            long horas = Math.abs(Duration.between(existente.getFecha(), concierto.getFecha()).toHours());
            if (horas < 48) {
                throw new IllegalOperationException(
                        "Debe haber al menos 2 días de diferencia entre conciertos en el mismo estadio");
            }
        }
    }
}


