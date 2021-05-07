package com.pe.ehuachaca.mockito.services;

import com.pe.ehuachaca.mockito.models.Examen;
import com.pe.ehuachaca.mockito.repository.ExamenRepository;
import com.pe.ehuachaca.mockito.repository.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {


    private ExamenRepository repository;
    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository repository, PreguntaRepository preguntaRepository) {
        this.repository = repository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return  repository.findAll()
                .stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = null;
        if (examenOptional.isPresent()){
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasPorExamenid(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {
        if (!examen.getPreguntas().isEmpty()){
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        return repository.guardar(examen);
    }
}
