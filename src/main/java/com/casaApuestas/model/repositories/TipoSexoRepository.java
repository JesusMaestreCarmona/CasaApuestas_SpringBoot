package com.casaApuestas.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.casaApuestas.model.entities.TipoSexo;

@Repository
public interface TipoSexoRepository extends CrudRepository<TipoSexo, Integer>{
	
}
