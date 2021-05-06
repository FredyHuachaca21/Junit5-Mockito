package com.pe.ehuachaca.mockito.services;

import com.pe.ehuachaca.mockito.models.Examen;
import com.pe.ehuachaca.mockito.repository.ExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {


    private ExamenRepository repository;

    public ExamenServiceImpl(ExamenRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return  repository.findAll()
                .stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();
    }
}
