package com.casaApuestas.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.casaApuestas.model.entities.Evento;

@Repository
public interface EventoRepository extends CrudRepository<Evento, Integer> {
		
	public Evento findByIdAndTerminado(int id, boolean terminado);
	
	public List<Evento> findByTerminadoOrderByFecha(boolean terminado);
	
	@Query(value = "SELECT * FROM evento WHERE fecha < ? AND terminado = 0", nativeQuery = true)
	public List<Evento> getEventosPasadosSinActualizar(Date fecha);
	
}
