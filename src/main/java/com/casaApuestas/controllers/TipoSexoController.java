package com.casaApuestas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casaApuestas.model.entities.TipoSexo;
import com.casaApuestas.model.repositories.TipoSexoRepository;

@CrossOrigin
@RestController
public class TipoSexoController {

	@Autowired
	TipoSexoRepository tipoSexoRepository;
	
	@GetMapping("tipoSexo/all")
	public Iterable<TipoSexo> getAllTiposSexo () {
		return this.tipoSexoRepository.findAll();
	}

}
