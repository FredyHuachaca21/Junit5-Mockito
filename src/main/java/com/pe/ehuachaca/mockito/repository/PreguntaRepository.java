package com.pe.ehuachaca.mockito.repository;

import java.util.List;

public interface PreguntaRepository {

    List<String> findPreguntasPorExamenid(Long id);
}
