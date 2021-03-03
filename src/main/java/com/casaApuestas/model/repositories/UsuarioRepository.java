package com.casaApuestas.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.casaApuestas.model.entities.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    public Usuario findByEmailAndPassword(String email, String password);
	
    public Usuario findByEmail(String email);
}
