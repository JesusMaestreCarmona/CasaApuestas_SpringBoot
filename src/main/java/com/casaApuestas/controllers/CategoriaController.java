package com.casaApuestas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casaApuestas.model.entities.Categoria;
import com.casaApuestas.model.repositories.CategoriaRepository;

@CrossOrigin
@RestController
public class CategoriaController {

	@Autowired
	CategoriaRepository categoriaRepository;
	
	@GetMapping("categoria/all")
	public Iterable<Categoria> getAllCategorias () {
		return this.categoriaRepository.findAll();
	}

}
