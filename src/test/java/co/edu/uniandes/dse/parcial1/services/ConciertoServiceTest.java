package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class ConciertoServiceTest {

    @Autowired
    private ConciertoService conciertoService;

    @Test
    void testCrearConciertoExitoso() throws IllegalOperationException {
        ConciertoEntity concierto = new ConciertoEntity();
        concierto.setNombreArtista("Shakira");
        concierto.setFecha(LocalDateTime.now().plusDays(5));
        concierto.setCapacidad(5000);
        concierto.setPresupuesto(200000);

        ConciertoEntity creado = conciertoService.createConcierto(concierto);

        assertNotNull(creado);
        assertEquals("Shakira", creado.getNombreArtista());
    }

    @Test
    void testCrearConciertoFechaPasada() {
        ConciertoEntity concierto = new ConciertoEntity();
        concierto.setNombreArtista("Metallica");
        concierto.setFecha(LocalDateTime.now().minusDays(1));
        concierto.setCapacidad(5000);
        concierto.setPresupuesto(200000);

        assertThrows(IllegalOperationException.class, () -> conciertoService.createConcierto(concierto));
    }

    @Test
    void testCrearConciertoCapacidadInvalida() {
        ConciertoEntity concierto = new ConciertoEntity();
        concierto.setNombreArtista("Soda Stereo");
        concierto.setFecha(LocalDateTime.now().plusDays(10));
        concierto.setCapacidad(5);
        concierto.setPresupuesto(200000);

        assertThrows(IllegalOperationException.class, () -> conciertoService.createConcierto(concierto));
    }

    @Test
    void testCrearConciertoPresupuestoInvalido() {
        ConciertoEntity concierto = new ConciertoEntity();
        concierto.setNombreArtista("Juanes");
        concierto.setFecha(LocalDateTime.now().plusDays(10));
        concierto.setCapacidad(100);
        concierto.setPresupuesto(500);

        assertThrows(IllegalOperationException.class, () -> conciertoService.createConcierto(concierto));
    }
}

