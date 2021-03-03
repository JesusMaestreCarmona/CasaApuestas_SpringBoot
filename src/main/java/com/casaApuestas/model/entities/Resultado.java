package com.casaApuestas.model.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the resultado database table.
 * 
 */
@Entity
@NamedQuery(name="Resultado.findAll", query="SELECT r FROM Resultado r")
public class Resultado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private int puntuacion1;

	private int puntuacion2;

	//bi-directional many-to-one association to Evento
	@OneToMany(mappedBy="resultadoBean")
	@JsonIgnore
	private List<Evento> eventos;

	//bi-directional many-to-one association to Participante
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ganador")
	@JsonIgnore
	private Participante ganador;

	public Resultado() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPuntuacion1() {
		return this.puntuacion1;
	}

	public void setPuntuacion1(int puntuacion1) {
		this.puntuacion1 = puntuacion1;
	}

	public int getPuntuacion2() {
		return this.puntuacion2;
	}

	public void setPuntuacion2(int puntuacion2) {
		this.puntuacion2 = puntuacion2;
	}

	public List<Evento> getEventos() {
		return this.eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public Evento addEvento(Evento evento) {
		getEventos().add(evento);
		evento.setResultadoBean(this);

		return evento;
	}

	public Evento removeEvento(Evento evento) {
		getEventos().remove(evento);
		evento.setResultadoBean(null);

		return evento;
	}

	public Participante getGanador() {
		return this.ganador;
	}

	public void setGanador(Participante ganador) {
		this.ganador = ganador;
	}

}