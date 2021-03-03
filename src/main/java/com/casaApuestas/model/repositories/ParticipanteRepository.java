package com.casaApuestas.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.casaApuestas.model.entities.Participante;

@Repository
public interface ParticipanteRepository extends CrudRepository<Participante, Integer> {

}
