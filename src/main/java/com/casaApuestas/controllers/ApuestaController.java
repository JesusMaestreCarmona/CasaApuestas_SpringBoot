package com.casaApuestas.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.casaApuestas.jwtSecurity.AutenticadorJWT;
import com.casaApuestas.model.entities.Apuesta;
import com.casaApuestas.model.entities.Categoria;
import com.casaApuestas.model.entities.Evento;
import com.casaApuestas.model.entities.Participante;
import com.casaApuestas.model.entities.Resultado;
import com.casaApuestas.model.entities.Usuario;
import com.casaApuestas.model.repositories.ApuestaRepository;
import com.casaApuestas.model.repositories.EventoRepository;
import com.casaApuestas.model.repositories.ParticipanteRepository;
import com.casaApuestas.model.repositories.UsuarioRepository;


@CrossOrigin
@RestController
public class ApuestaController {

	@Autowired
	ApuestaRepository apuestaRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	EventoRepository eventoRepository;
	@Autowired
	ParticipanteRepository participanteRepository;
	
	@GetMapping("apuestas/findById")
	public DTO findById(int id) {
		DTO dto = new DTO();
		dto.put("result", "fail");
		try {
			Apuesta apuesta = this.apuestaRepository.findByIdapuestaAndNotificada(id, false);
			if (apuesta != null) {
				dto.put("apuesta", getDTOFromApuesta(apuesta));
				dto.put("result", "ok");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@GetMapping("apuestas/getApuestasUsuario")
	public DTO getApuestasUsuario(boolean terminado, HttpServletRequest request) {
		DTO dto = new DTO();
		dto.put("result", "fail");
		try {
			int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);
			Usuario usuario = this.usuarioRepository.findById(idUsuAutenticado).get();
			List<Apuesta> apuestasUsuario = this.apuestaRepository.findByUsuarioBeanAndEventoBeanTerminadoOrderByEventoBeanFecha(usuario, terminado);
			List<DTO> apuestasUsuarioDTO = new ArrayList<DTO>();
			for (Apuesta apuesta : apuestasUsuario) {
				apuestasUsuarioDTO.add(getDTOFromApuesta(apuesta));
			}
			dto.put("apuestas", apuestasUsuarioDTO);
			dto.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@GetMapping("apuestas/getApuestasUsuarioANotificar")
	public DTO getApuestasUsuarioANotificar(HttpServletRequest request) {
		DTO dto = new DTO();
		dto.put("result", "fail");
		try {
			int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);
			Usuario usuario = this.usuarioRepository.findById(idUsuAutenticado).get();
			List<Apuesta> apuestasANotificar = this.apuestaRepository.findByUsuarioBeanAndEventoBeanTerminadoAndNotificada(usuario, true, false);
			List<DTO> apuestasUsuarioDTO = new ArrayList<DTO>();
			for (Apuesta apuesta : apuestasANotificar) {
				calcularPremio(apuesta);
				apuesta.setNotificada(true);
				this.apuestaRepository.save(apuesta);
				apuestasUsuarioDTO.add(getDTOFromApuesta(apuesta));
			}
			dto.put("apuestasANotificar", apuestasUsuarioDTO);
			dto.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@PutMapping("apuestas/nueva")
	public DTO nuevaApuesta (@RequestBody DatosParaNuevaApuesta datosNuevaApuesta, HttpServletRequest request) {
		DTO dto = new DTO();
		dto.put("result", "fail");

		try {
			int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);
			Usuario usuarioAutenticado = this.usuarioRepository.findById(idUsuAutenticado).get();
			Apuesta apuestaAGuardar = (datosNuevaApuesta.idapuesta != 0)? this.apuestaRepository.findById(datosNuevaApuesta.idapuesta).get() : new Apuesta();
			apuestaAGuardar.setFecha(new Date());
			usuarioAutenticado.setSaldo(usuarioAutenticado.getSaldo() - datosNuevaApuesta.cantidadApostada + apuestaAGuardar.getCantidadApostada());
			apuestaAGuardar.setCantidadApostada(datosNuevaApuesta.cantidadApostada);
			apuestaAGuardar.setUsuarioBean(usuarioAutenticado);
			apuestaAGuardar.setCuota(datosNuevaApuesta.cuota);
			Participante ganador = (datosNuevaApuesta.ganador != 0)? this.participanteRepository.findById(datosNuevaApuesta.ganador).get() : null;
			apuestaAGuardar.setGanador(ganador);
			apuestaAGuardar.setEventoBean(eventoRepository.findById(datosNuevaApuesta.evento).get());
			apuestaAGuardar.setPuntuacion1(datosNuevaApuesta.puntuacion1);
			apuestaAGuardar.setPuntuacion2(datosNuevaApuesta.puntuacion2);
			apuestaAGuardar.setPremioPotencial(datosNuevaApuesta.premioPotencial);
			this.usuarioRepository.save(usuarioAutenticado);
			this.apuestaRepository.save(apuestaAGuardar);
			dto.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@DeleteMapping("apuestas/borrar")
	public DTO borrarApuesta(int id, HttpServletRequest request) {
		DTO dto = new DTO();
		dto.put("result", "fail");
		try {
			int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);
			Usuario usuarioAutenticado = this.usuarioRepository.findById(idUsuAutenticado).get();
			Apuesta apuestaABorrar = this.apuestaRepository.findById(id).get();
			usuarioAutenticado.setSaldo(usuarioAutenticado.getSaldo() + apuestaABorrar.getCantidadApostada());
			this.usuarioRepository.save(usuarioAutenticado);
			this.apuestaRepository.delete(apuestaABorrar);
			dto.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	private void calcularPremio(Apuesta apuesta) {
		Resultado resultadoReal = apuesta.getEventoBean().getResultadoBean();
		if (resultadoReal.getGanador() == apuesta.getGanador()) {
			if (resultadoReal.getPuntuacion1() == apuesta.getPuntuacion1() && resultadoReal.getPuntuacion2() == apuesta.getPuntuacion2())
				apuesta.setPremio(apuesta.getPremioPotencial());
			else
				apuesta.setPremio(apuesta.getPremioPotencial()/2);
			Usuario usuario = apuesta.getUsuarioBean();
			usuario.setSaldo(usuario.getSaldo() + apuesta.getPremio());
			this.usuarioRepository.save(usuario);
			this.apuestaRepository.save(apuesta);		
		}
	}
	
	private DTO getDTOFromApuesta(Apuesta a) {
		DTO dto = new DTO();
		dto.put("idapuesta", a.getIdapuesta());
		dto.put("fecha", a.getFecha());
		dto.put("cantidadApostada", a.getCantidadApostada());
		dto.put("cuota", a.getCuota());
		dto.put("puntuacion1", a.getPuntuacion1());
		dto.put("puntuacion2", a.getPuntuacion2());
		DTO ganador = (a.getGanador() != null)? getDtoFromParticipanteMinimosDatos(a.getGanador()) : null;
		dto.put("ganador", ganador);
		dto.put("evento", getDTOFromEventos(a.getEventoBean()));
		dto.put("premio", a.getPremio());
		dto.put("premioPotencial", a.getPremioPotencial());
		dto.put("notificada", a.getNotificada());
		return dto;
	}
		
	private DTO getDTOFromEventos(Evento e) {
		DTO dto = new DTO();
		dto.put("id", e.getId());
		dto.put("fecha", e.getFecha());
		dto.put("categoria", getDtoFromCategoria(e.getCategoriaBean()));
		dto.put("participante1", getDtoFromParticipanteMinimosDatos(e.getParticipante1Bean()));
		dto.put("participante2", getDtoFromParticipanteMinimosDatos(e.getParticipante2Bean()));
		dto.put("resultado", getDTOFromResultado(e.getResultadoBean()));
		dto.put("terminado", e.getTerminado());
		return dto;
	}
	
	private DTO getDTOFromResultado(Resultado r) {
		DTO dto = new DTO();
		dto.put("id", r.getId());
		dto.put("puntuacion1", r.getPuntuacion1());
		dto.put("puntuacion2", r.getPuntuacion2());
		dto.put("ganador", getDtoFromParticipanteMinimosDatos(r.getGanador()));
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

class DatosParaNuevaApuesta {
	
	int idapuesta;
	float cantidadApostada;
	int ganador;
	float cuota;
	int evento;
	int puntuacion1;
	int puntuacion2;
	int premioPotencial;
	
	public DatosParaNuevaApuesta(int idapuesta, float cantidadApostada, int ganador, float cuota, int evento, int puntuacion1, int puntuacion2, int premioPotencial) {
		super();
		this.idapuesta = idapuesta;
		this.cantidadApostada = cantidadApostada;
		this.ganador = ganador;
		this.cuota = cuota;
		this.evento = evento;
		this.puntuacion1 = puntuacion1;
		this.puntuacion2 = puntuacion2;
		this.premioPotencial = premioPotencial;
	}
}
