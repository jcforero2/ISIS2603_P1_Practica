package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.ConciertoRepository;
import co.edu.uniandes.dse.parcial1.repositories.EstadioRepository;

@SpringBootTest
@Transactional
class ConciertoEstadioServiceTest {

    @Autowired
    private ConciertoEstadioService conciertoEstadioService;

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Autowired
    private EstadioRepository estadioRepository;

    private ConciertoEntity concierto;
    private EstadioEntity estadio;

    @BeforeEach
    void setUp() {
        concierto = new ConciertoEntity();
        concierto.setNombreArtista("Queen");
        concierto.setFecha(LocalDateTime.now().plusDays(5));
        concierto.setCapacidad(1000);
        concierto.setPresupuesto(300000);
        conciertoRepository.save(concierto);

        estadio = new EstadioEntity();
        estadio.setCiudad("Bogotá");
        estadio.setAforoMax(5000);
        estadio.setPrecioAlquiler(200000);
        estadioRepository.save(estadio);
    }

    @Test
    void testAsociarExitoso() throws IllegalOperationException {
        ConciertoEntity asociado = conciertoEstadioService.assignEstadio(concierto.getId(), estadio.getId());
        assertNotNull(asociado.getEstadio());
        assertEquals(estadio.getId(), asociado.getEstadio().getId());
    }

    @Test
    void testAsociarConciertoNoExiste() {
        assertThrows(IllegalOperationException.class,
            () -> conciertoEstadioService.assignEstadio(999L, estadio.getId()));
    }

    @Test
    void testAsociarEstadioNoExiste() {
        assertThrows(IllegalOperationException.class,
            () -> conciertoEstadioService.assignEstadio(concierto.getId(), 999L));
    }

    @Test
    void testAsociarCapacidadExcedeAforo() {
        concierto.setCapacidad(6000);
        conciertoRepository.save(concierto);

        assertThrows(IllegalOperationException.class,
            () -> conciertoEstadioService.assignEstadio(concierto.getId(), estadio.getId()));
    }

    @Test
    void testAsociarPrecioMayorPresupuesto() {
        estadio.setPrecioAlquiler(400000);
        estadioRepository.save(estadio);

        assertThrows(IllegalOperationException.class,
            () -> conciertoEstadioService.assignEstadio(concierto.getId(), estadio.getId()));
    }

    @Test
    void testAsociarConciertosMuyCercanos() throws IllegalOperationException {
        // primer concierto
        conciertoEstadioService.assignEstadio(concierto.getId(), estadio.getId());

        // segundo concierto con <48h de diferencia
        ConciertoEntity otro = new ConciertoEntity();
        otro.setNombreArtista("Coldplay");
        otro.setFecha(concierto.getFecha().plusHours(24)); // solo 1 día después
        otro.setCapacidad(1000);
        otro.setPresupuesto(300000);
        conciertoRepository.save(otro);

        assertThrows(IllegalOperationException.class,
            () -> conciertoEstadioService.assignEstadio(otro.getId(), estadio.getId()));
    }
}

