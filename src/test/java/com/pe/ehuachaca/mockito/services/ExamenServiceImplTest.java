package com.pe.ehuachaca.mockito.services;

import com.pe.ehuachaca.mockito.models.Examen;
import com.pe.ehuachaca.mockito.repository.ExamenRepository;
import com.pe.ehuachaca.mockito.repository.ExamenRepositoryOther;
import com.pe.ehuachaca.mockito.repository.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {

    ExamenRepository repository;
    ExamenService service;
    PreguntaRepository preguntaRepository;

    /*Se ejecuta antes de cada prueba*/
    @BeforeEach
    void setUp() {
        //ExamenRepository repository = new ExamenRepositoryImpl();
        repository = mock(ExamenRepositoryOther.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        List<Examen> datos = Collections.emptyList();
        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenid(5L)).thenReturn(Datos.PREGUNTAS);
        /*Otra alternativa de obtener o validar el ID*/
        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("trigonometría"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenid(5L)).thenReturn(Datos.PREGUNTAS);
        /*Otra alternativa de obtener o validar el ID*/
        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("trigonometría"));
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenid(anyLong());
        verify(preguntaRepository).findPreguntasPorExamenid(5L);
    }

    /*Forzando la falla*/
    @Test
    void testNoExisteExamenVerify() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenid(5L)).thenReturn(Datos.PREGUNTAS);
        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertNull(examen);
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenid(anyLong());
        verify(preguntaRepository).findPreguntasPorExamenid(5L);
    }
}