package com.casaApuestas.model.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.casaApuestas.model.entities.Apuesta;
import com.casaApuestas.model.entities.Usuario;

@Repository
public interface ApuestaRepository extends CrudRepository<Apuesta, Integer> {
	
	public Apuesta findByIdapuestaAndNotificada(int id, boolean notificada);
	
    public List<Apuesta> findByUsuarioBeanAndEventoBeanTerminadoOrderByEventoBeanFecha(Usuario usuario, boolean terminado);
	
    public List<Apuesta> findByUsuarioBeanAndEventoBeanTerminadoAndNotificada(Usuario usuario, boolean terminado, boolean notificada);
    
}
