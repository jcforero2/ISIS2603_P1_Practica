package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class EstadioServiceTest {

    @Autowired
    private EstadioService estadioService;

    @Test
    void testCrearEstadioExitoso() throws IllegalOperationException {
        EstadioEntity estadio = new EstadioEntity();
        estadio.setCiudad("Bogotá");
        estadio.setAforoMax(50000);
        estadio.setPrecioAlquiler(200000);

        EstadioEntity creado = estadioService.createEstadio(estadio);

        assertNotNull(creado);
        assertEquals("Bogotá", creado.getCiudad());
    }

    @Test
    void testCrearEstadioCiudadInvalida() {
        EstadioEntity estadio = new EstadioEntity();
        estadio.setCiudad("NY");
        estadio.setAforoMax(50000);
        estadio.setPrecioAlquiler(200000);

        assertThrows(IllegalOperationException.class, () -> estadioService.createEstadio(estadio));
    }

    @Test
    void testCrearEstadioAforoInvalido() {
        EstadioEntity estadio = new EstadioEntity();
        estadio.setCiudad("Medellín");
        estadio.setAforoMax(500); // inválido
        estadio.setPrecioAlquiler(200000);

        assertThrows(IllegalOperationException.class, () -> estadioService.createEstadio(estadio));
    }

    @Test
    void testCrearEstadioPrecioInvalido() {
        EstadioEntity estadio = new EstadioEntity();
        estadio.setCiudad("Cali");
        estadio.setAforoMax(50000);
        estadio.setPrecioAlquiler(50000); // inválido

        assertThrows(IllegalOperationException.class, () -> estadioService.createEstadio(estadio));
    }
}

