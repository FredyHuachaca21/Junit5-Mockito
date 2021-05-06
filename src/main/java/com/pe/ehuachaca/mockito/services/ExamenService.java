package com.pe.ehuachaca.mockito.services;

import com.pe.ehuachaca.mockito.models.Examen;

public interface ExamenService {
    Examen findExamenPorNombre(String nombre);
}
