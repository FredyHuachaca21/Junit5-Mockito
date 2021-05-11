package com.pe.ehuachaca.mockito.services;

import com.pe.ehuachaca.mockito.Datos;
import com.pe.ehuachaca.mockito.models.Examen;
import com.pe.ehuachaca.mockito.repository.ExamenRepository;
import com.pe.ehuachaca.mockito.repository.PreguntaRepository;
import com.pe.ehuachaca.mockito.repositoryImpl.ExamenRepositoryImpl;
import com.pe.ehuachaca.mockito.repositoryImpl.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {

    @Spy
    ExamenRepositoryImpl repository;

    @Spy
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;



    @Test
    void testSpy() {

        List<String> preguntas = Arrays.asList("trigomometría");
//        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenid(anyLong());
        ExamenService examenService = new ExamenServiceImpl(repository, preguntaRepository);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("trigomometría"));

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenid(anyLong());
    }
}