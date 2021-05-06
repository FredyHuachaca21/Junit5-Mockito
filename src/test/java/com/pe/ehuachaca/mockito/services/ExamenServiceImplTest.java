package com.pe.ehuachaca.mockito.services;

import com.pe.ehuachaca.mockito.models.Examen;
import com.pe.ehuachaca.mockito.repository.ExamenRepository;
import com.pe.ehuachaca.mockito.repository.ExamenRepositoryOther;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {

    @Test
    void findExamenPorNombre() {
        //ExamenRepository repository = new ExamenRepositoryImpl();
        ExamenRepository repository = mock(ExamenRepositoryOther.class);
        ExamenService service = new ExamenServiceImpl(repository);

        List<Examen> datos = Arrays.asList(
                new Examen(5L, "Matematicas"),
                new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"));
        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        //ExamenRepository repository = new ExamenRepositoryImpl();
        ExamenRepository repository = mock(ExamenRepositoryOther.class);
        ExamenService service = new ExamenServiceImpl(repository);

        List<Examen> datos = Collections.emptyList();
        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertFalse(examen.isPresent());
    }
}