package com.casaApuestas.model.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;


/**
 * The persistent class for the apuesta database table.
 * 
 */
@Entity
@NamedQuery(name="Apuesta.findAll", query="SELECT a FROM Apuesta a")
public class Apuesta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idapuesta;

	@Column(name="cantidad_apostada")
	private float cantidadApostada;

	private float cuota;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private boolean notificada;

	private float premio;

	private float premioPotencial;

	private int puntuacion1;

	private int puntuacion2;

	//bi-directional many-to-one association to Evento
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="evento")
	@JsonIgnore
	private Evento eventoBean;

	//bi-directional many-to-one association to Participante
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ganador")
	@JsonIgnore
	private Participante ganador;

	//bi-directional many-to-one association to Usuario
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="usuario")
	@JsonIgnore
	private Usuario usuarioBean;

	public Apuesta() {
	}

	public int getIdapuesta() {
		return this.idapuesta;
	}

	public void setIdapuesta(int idapuesta) {
		this.idapuesta = idapuesta;
	}

	public float getCantidadApostada() {
		return this.cantidadApostada;
	}

	public void setCantidadApostada(float cantidadApostada) {
		this.cantidadApostada = cantidadApostada;
	}

	public float getCuota() {
		return this.cuota;
	}

	public void setCuota(float cuota) {
		this.cuota = cuota;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean getNotificada() {
		return this.notificada;
	}

	public void setNotificada(boolean notificada) {
		this.notificada = notificada;
	}

	public float getPremio() {
		return this.premio;
	}

	public void setPremio(float premio) {
		this.premio = premio;
	}

	public float getPremioPotencial() {
		return this.premioPotencial;
	}

	public void setPremioPotencial(float premioPotencial) {
		this.premioPotencial = premioPotencial;
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

	public Evento getEventoBean() {
		return this.eventoBean;
	}

	public void setEventoBean(Evento eventoBean) {
		this.eventoBean = eventoBean;
	}

	public Participante getGanador() {
		return this.ganador;
	}

	public void setGanador(Participante ganador) {
		this.ganador = ganador;
	}

	public Usuario getUsuarioBean() {
		return this.usuarioBean;
	}

	public void setUsuarioBean(Usuario usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

}