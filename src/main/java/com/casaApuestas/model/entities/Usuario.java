package com.casaApuestas.model.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String apellidos;

	private String email;

	@Lob
	private byte[] imagen;

	private String nombre;

	private String password;

	private float saldo;

	//bi-directional many-to-one association to Apuesta
	@OneToMany(mappedBy="usuarioBean")
	@JsonIgnore
	private List<Apuesta> apuestas;

	//bi-directional many-to-one association to TipoSexo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tiposexo")
	@JsonIgnore
	private TipoSexo tiposexoBean;

	public Usuario() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getImagen() {
		return this.imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public float getSaldo() {
		return this.saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	public List<Apuesta> getApuestas() {
		return this.apuestas;
	}

	public void setApuestas(List<Apuesta> apuestas) {
		this.apuestas = apuestas;
	}

	public Apuesta addApuesta(Apuesta apuesta) {
		getApuestas().add(apuesta);
		apuesta.setUsuarioBean(this);

		return apuesta;
	}

	public Apuesta removeApuesta(Apuesta apuesta) {
		getApuestas().remove(apuesta);
		apuesta.setUsuarioBean(null);

		return apuesta;
	}

	public TipoSexo getTiposexoBean() {
		return this.tiposexoBean;
	}

	public void setTiposexoBean(TipoSexo tiposexoBean) {
		this.tiposexoBean = tiposexoBean;
	}

}