package com.pe.ehuachaca.mockito.repository;

import com.pe.ehuachaca.mockito.models.Examen;

import java.util.List;

public interface ExamenRepository {

    Examen guardar(Examen examen);
    List<Examen> findAll();
}
