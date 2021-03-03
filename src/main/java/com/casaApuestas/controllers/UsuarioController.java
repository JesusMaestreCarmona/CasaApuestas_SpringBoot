package com.casaApuestas.controllers;


import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.casaApuestas.jwtSecurity.AutenticadorJWT;
import com.casaApuestas.model.entities.TipoSexo;
import com.casaApuestas.model.entities.Usuario;
import com.casaApuestas.model.repositories.TipoSexoRepository;
import com.casaApuestas.model.repositories.UsuarioRepository;
import com.casaApuestas.services.EmailService;


@CrossOrigin
@RestController
public class UsuarioController {

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	TipoSexoRepository tipoSexoRepository;
	@Autowired
	EmailService emailService;
	
	/**
	 * Autentica un usuario, dados su datos de acceso: nombre de usuario y contraseña
	 */
	@PostMapping("/usuario/autenticar")
	public DTO autenticaUsuario (@RequestBody DatosAutenticacionUsuario datos) {
		DTO dto = new DTO(); // Voy a devolver un dto

		// Intento localizar un usuario a partir de su nombre de usuario y su password
		Usuario usuAutenticado = usuarioRepository.findByEmailAndPassword(datos.email, datos.password);
		if (usuAutenticado != null) {
			dto.put("jwt", AutenticadorJWT.codificaJWT(usuAutenticado));
		}

		// Finalmente devuelvo el JWT creado, puede estar vacío si la autenticación no ha funcionado
		return dto;
	}
	
	@GetMapping("/usuario/buscarEmail")
	public DTO buscarEmail(String email) {
		DTO dto = new DTO();
		boolean emailEncontrado = false;
		if (this.usuarioRepository.findByEmail(email) != null) emailEncontrado = true;
		dto.put("emailEncontrado", emailEncontrado);
		return dto;
	}
	
	@GetMapping("/usuario/getAutenticado")
	public DTO getUsuarioAutenticado (boolean imagen, HttpServletRequest request) {
		DTO dtoResultado = null;
		
		int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request); // Obtengo el usuario autenticado, por su JWT
		
		// Intento localizar un usuario a partir de su id
		if (idUsuAutenticado != -1) {
			Usuario usuAutenticado = usuarioRepository.findById(idUsuAutenticado).get();
			dtoResultado = getDTOFromUsuario(usuAutenticado, imagen);
		}

		// Finalmente devuelvo el JWT creado, puede estar vacío si la autenticación no ha funcionado
		return dtoResultado;
	}
	
	@PutMapping("/usuario/registrarUsuario")
	public DTO registrarUsuario (@RequestBody DatosParaNuevoUsuario datosNuevoUsuario) {
		DTO dto = new DTO();
		dto.put("result", "fail");
		try {
			Usuario usuarioARegistrar = new Usuario();
			usuarioARegistrar.setEmail(datosNuevoUsuario.email);
			usuarioARegistrar.setPassword(datosNuevoUsuario.password);
			usuarioARegistrar.setNombre(datosNuevoUsuario.nombre);
			usuarioARegistrar.setApellidos(datosNuevoUsuario.apellidos);
			if (datosNuevoUsuario.imagen != null) usuarioARegistrar.setImagen(Base64.decodeBase64(datosNuevoUsuario.imagen));
			usuarioARegistrar.setSaldo(1000);
			usuarioARegistrar.setTiposexoBean(this.tipoSexoRepository.findById(datosNuevoUsuario.tipoSexo).get());
			this.usuarioRepository.save(usuarioARegistrar);
			dto.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@PutMapping("/usuario/actualizarDatosUsuario")
	public DTO actualizarDatosUsuario(@RequestBody DatosParaNuevoUsuario datosNuevoUsuario, HttpServletRequest request) {
		DTO dto = new DTO();
		dto.put("result", "fail");
		try {
			int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);
			if (idUsuAutenticado != -1) {
				Usuario usuAutenticado = usuarioRepository.findById(idUsuAutenticado).get();
				usuAutenticado.setNombre(datosNuevoUsuario.nombre);
				usuAutenticado.setApellidos(datosNuevoUsuario.apellidos);
				usuAutenticado.setImagen(Base64.decodeBase64(datosNuevoUsuario.imagen));
				usuAutenticado.setTiposexoBean(this.tipoSexoRepository.findById(datosNuevoUsuario.tipoSexo).get());
				this.usuarioRepository.save(usuAutenticado);
			}
			dto.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@PutMapping("/usuario/modificarPassword")
	public DTO modificarPassword(@RequestBody DatosParaNuevoUsuario datosNuevoUsuario, HttpServletRequest request) {
		DTO dto = new DTO();
		dto.put("result", "fail");
		try {
			int idUsuAutenticado = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);
			if (idUsuAutenticado != -1) {
				Usuario usuAutenticado = usuarioRepository.findById(idUsuAutenticado).get();
				usuAutenticado.setPassword(datosNuevoUsuario.password);
				this.usuarioRepository.save(usuAutenticado);
			}
			dto.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	private DTO getDTOFromUsuario (Usuario usu, boolean incluirImagen) {
		DTO dto = new DTO(); // Voy a devolver un dto
		if (usu != null) {
			dto.put("id", usu.getId());
			dto.put("email", usu.getEmail());
			dto.put("password", usu.getPassword());
			dto.put("nombre", usu.getNombre());
			dto.put("apellidos", usu.getApellidos());
			dto.put("tiposexo", getDtoFromTipoSexo(usu.getTiposexoBean()));
			dto.put("saldo", usu.getSaldo());
			if (incluirImagen)
				dto.put("imagen", usu.getImagen());
		}
		return dto;
	}
	
	private DTO getDtoFromTipoSexo (TipoSexo ts) {
		DTO dto = new DTO();
		dto.put("id", ts.getId());
		dto.put("descripcion", ts.getDescripcion());
		return dto;
	}

}

/**
 * Clase que contiene los datos de autenticacion del usuario
 */
class DatosAutenticacionUsuario {
	String email;
	String password;

	/**
	 * Constructor
	 */
	public DatosAutenticacionUsuario(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
}


class DatosParaNuevoUsuario {
	
	String email;
	String password;
	String nombre;
	String apellidos;
	String imagen;
	int tipoSexo;
	
	public DatosParaNuevoUsuario(String email, String password, String nombre, String apellidos, String imagen, int tipoSexo) {
		super();
		this.email = email;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.imagen = imagen;
		this.tipoSexo = tipoSexo;
	}
}

