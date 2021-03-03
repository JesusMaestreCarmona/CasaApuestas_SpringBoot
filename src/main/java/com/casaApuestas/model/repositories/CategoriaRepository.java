package com.casaApuestas.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.casaApuestas.model.entities.Categoria;


@Repository
public interface CategoriaRepository extends CrudRepository<Categoria, Integer> {

}
