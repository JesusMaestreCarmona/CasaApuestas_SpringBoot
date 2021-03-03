package com.casaApuestas.model.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the participante database table.
 * 
 */
@Entity
@NamedQuery(name="Participante.findAll", query="SELECT p FROM Participante p")
public class Participante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Lob
	private byte[] imagen;

	private String nombre;

	//bi-directional many-to-one association to Apuesta
	@OneToMany(mappedBy="ganador")
	@JsonIgnore
	private List<Apuesta> apuestas;

	//bi-directional many-to-one association to Evento
	@OneToMany(mappedBy="participante1Bean")
	@JsonIgnore
	private List<Evento> eventos1;

	//bi-directional many-to-one association to Evento
	@OneToMany(mappedBy="participante2Bean")
	@JsonIgnore
	private List<Evento> eventos2;

	//bi-directional many-to-one association to Resultado
	@OneToMany(mappedBy="ganador")
	@JsonIgnore
	private List<Resultado> resultados;

	public Participante() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public List<Apuesta> getApuestas() {
		return this.apuestas;
	}

	public void setApuestas(List<Apuesta> apuestas) {
		this.apuestas = apuestas;
	}

	public Apuesta addApuesta(Apuesta apuesta) {
		getApuestas().add(apuesta);
		apuesta.setGanador(this);

		return apuesta;
	}

	public Apuesta removeApuesta(Apuesta apuesta) {
		getApuestas().remove(apuesta);
		apuesta.setGanador(null);

		return apuesta;
	}

	public List<Evento> getEventos1() {
		return this.eventos1;
	}

	public void setEventos1(List<Evento> eventos1) {
		this.eventos1 = eventos1;
	}

	public Evento addEventos1(Evento eventos1) {
		getEventos1().add(eventos1);
		eventos1.setParticipante1Bean(this);

		return eventos1;
	}

	public Evento removeEventos1(Evento eventos1) {
		getEventos1().remove(eventos1);
		eventos1.setParticipante1Bean(null);

		return eventos1;
	}

	public List<Evento> getEventos2() {
		return this.eventos2;
	}

	public void setEventos2(List<Evento> eventos2) {
		this.eventos2 = eventos2;
	}

	public Evento addEventos2(Evento eventos2) {
		getEventos2().add(eventos2);
		eventos2.setParticipante2Bean(this);

		return eventos2;
	}

	public Evento removeEventos2(Evento eventos2) {
		getEventos2().remove(eventos2);
		eventos2.setParticipante2Bean(null);

		return eventos2;
	}

	public List<Resultado> getResultados() {
		return this.resultados;
	}

	public void setResultados(List<Resultado> resultados) {
		this.resultados = resultados;
	}

	public Resultado addResultado(Resultado resultado) {
		getResultados().add(resultado);
		resultado.setGanador(this);

		return resultado;
	}

	public Resultado removeResultado(Resultado resultado) {
		getResultados().remove(resultado);
		resultado.setGanador(null);

		return resultado;
	}

}