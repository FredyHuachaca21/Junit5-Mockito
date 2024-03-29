package com.pe.ehuachaca.mockito.services;

import com.pe.ehuachaca.mockito.Datos;
import com.pe.ehuachaca.mockito.models.Examen;
import com.pe.ehuachaca.mockito.repository.ExamenRepository;
import com.pe.ehuachaca.mockito.repositoryImpl.ExamenRepositoryImpl;
import com.pe.ehuachaca.mockito.repository.PreguntaRepository;
import com.pe.ehuachaca.mockito.repositoryImpl.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepositoryImpl repository;

    @Mock
    PreguntaRepositoryImpl preguntaRepository;

    /*@Mock
    ExamenRepositoryImpl examenRepositoryImpl;

    @Mock
    PreguntaRepositoryImpl preguntaRepositoryImpl;*/

    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    /*Se ejecuta antes de cada prueba*/
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //ExamenRepository repository = new ExamenRepositoryImpl();
        /*repository = mock(ExamenRepositoryOther.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(repository, preguntaRepository);*/
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
        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("trigonometría"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasPorExamenid(5L)).thenReturn(Datos.PREGUNTAS);
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

    @Test
    void testGuardarExamen() {
        //GIVEN => Son precondiciones para el entorno de pruebas
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        /*Cuando se invoque guardar se le asigna un id incremental*/
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });
        //When
        Examen examen = service.guardar(newExamen);
        //then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }
    @Test
    void testManejoException() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenid(anyLong());
    }

    @Test
    void testManejoExceptionConNull() {
     when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
    // when(preguntaRepository.findPreguntasPorExamenid(null)).thenThrow(IllegalArgumentException.class);
     when(preguntaRepository.findPreguntasPorExamenid(isNull())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(repository).findAll();
       // verify(preguntaRepository).findPreguntasPorExamenid(null);
        verify(preguntaRepository).findPreguntasPorExamenid(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
//        verify(preguntaRepository).findPreguntasPorExamenid(argThat(arg-> arg != null && arg.equals(5L)));
//        verify(preguntaRepository).findPreguntasPorExamenid(eq(5L));
        verify(preguntaRepository).findPreguntasPorExamenid(argThat(arg-> arg != null && arg >=2L));
    }

    @Test
    void testArgumentMatchers2() {
        /*Para que pase la prueba del test se debe cambiar con datos positovos*/
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenid(argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentMatchers3() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        /*Para que pase la prueba se debe cambiar con datos positovos*/
//        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenid(argThat((args) -> args != null && args >0));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long>{

        private Long argumento;

        @Override
        public boolean matches(Long aLong) {
            this.argumento = aLong;
            return aLong != null && aLong >0;
        }

        @Override
        public String toString() {
            return "Es para un mensaje personalizado de error que imprime mockito en caso que falle el test " +
                    argumento +
                    " debe ser un entero posito";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class); /*<- Reemplaza anotación*/
        verify(preguntaRepository).findPreguntasPorExamenid(captor.capture());

        assertEquals(5L, captor.getValue());

    }

    @Test
    void tesDoThrow() {
        /*Comprueba excepciones para métodos void*/
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());
        assertThrows(IllegalArgumentException.class, ()-> service.guardar(examen));
    }

    @Test
    void testDoAnswer() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        doAnswer(invocationOnMock -> {
           Long id = invocationOnMock.getArgument(0);
           return id == 5L ? Datos.PREGUNTAS: Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamenid(anyLong());
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("geometría"));

        verify(preguntaRepository).findPreguntasPorExamenid(anyLong());

    }

    @Test
    void testDoAnswerGuardarExamen() {
        //GIVEN => Son precondiciones para el entorno de pruebas
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        /*Cuando se invoque guardar se le asigna un id incremental*/
        doAnswer(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(repository).guardar(any(Examen.class));

        //When
        Examen examen = service.guardar(newExamen);
        //then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenid(anyLong());
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    @Test
    void testSpy() {
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);

        List<String> preguntas = Arrays.asList("trigomometría");
//        when(preguntaRepository.findPreguntasPorExamenid(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenid(anyLong());
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("trigomometría"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenid(anyLong());
    }
}