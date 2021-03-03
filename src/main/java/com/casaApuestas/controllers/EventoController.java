package com.casaApuestas.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casaApuestas.model.entities.Apuesta;
import com.casaApuestas.model.entities.Categoria;
import com.casaApuestas.model.entities.Evento;
import com.casaApuestas.model.entities.Participante;
import com.casaApuestas.model.repositories.EventoRepository;

@CrossOrigin
@RestController
public class EventoController {

	@Autowired
	EventoRepository eventoRepository;
	
	@GetMapping("eventos/all")
	public DTO getAllEventos() {
		DTO dto = new DTO();
		dto.put("result", "fail");	
		try {	
			List<DTO> listaEventosEnDTO = new ArrayList<DTO>();
			List<Evento> eventos = new ArrayList<Evento>();
			eventos = (List<Evento>) this.eventoRepository.findByTerminadoOrderByFecha(false);
			for (Evento e : eventos) {
				// Agrego el dto con el mensaje completo, con todos los datos trabajado
				listaEventosEnDTO.add(getDTOFromEventos(e));
			}
			dto.put("eventos", listaEventosEnDTO);
			dto.put("result", "ok");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@GetMapping("eventos/findById")
	public DTO findEventoById(int id) {
		DTO dto = new DTO();
		dto.put("result", "fail");
		try {
			Evento evento = this.eventoRepository.findByIdAndTerminado(id, false);
			if (evento != null) {
				dto.put("evento", getDTOFromEventos(evento));
				dto.put("result", "ok");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@GetMapping("eventos/actualizarLista") 
	public DTO actualizarListaEventos() {
		DTO dto = new DTO();
		dto.put("result", "fail");	
		try {
			List<Evento> eventosPasadosSinActualizar = this.eventoRepository.getEventosPasadosSinActualizar(new Date());
			for (Evento evento : eventosPasadosSinActualizar) {
				evento.setTerminado(true);
				this.eventoRepository.save(evento);
			}
			dto.put("result", "ok");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	private DTO getDTOFromEventos(Evento e) {
		DTO dto = new DTO();
		dto.put("id", e.getId());
		dto.put("cuota1", e.getCuota1());
		dto.put("cuota2", e.getCuota2());
		dto.put("cuotax", e.getCuotaX());
		dto.put("fecha", e.getFecha());
		dto.put("categoria", getDtoFromCategoria(e.getCategoriaBean()));
		dto.put("participante1", getDtoFromParticipanteMinimosDatos(e.getParticipante1Bean()));
		dto.put("participante2", getDtoFromParticipanteMinimosDatos(e.getParticipante2Bean()));
		dto.put("terminado", e.getTerminado());
		return dto;
	}
	
	private DTO getDtoFromParticipanteMinimosDatos (Participante p) {
		DTO dto = new DTO();
		dto.put("id", p.getId());
		dto.put("nombre", p.getNombre());
		dto.put("imagen", p.getImagen());
		return dto;
	}
	
	private DTO getDtoFromCategoria (Categoria c) {
		DTO dto = new DTO();
		dto.put("id", c.getId());
		dto.put("descripcion", c.getDescripcion());
		return dto;
	}

}