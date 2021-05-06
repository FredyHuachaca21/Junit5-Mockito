package com.pe.ehuachaca.mockito.services;

import com.pe.ehuachaca.mockito.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenPorNombre(String nombre);
}
