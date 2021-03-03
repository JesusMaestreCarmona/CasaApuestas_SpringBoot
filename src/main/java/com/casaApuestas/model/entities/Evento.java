package com.casaApuestas.model.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the evento database table.
 * 
 */
@Entity
@NamedQuery(name="Evento.findAll", query="SELECT e FROM Evento e")
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private float cuota1;

	private float cuota2;

	private float cuotaX;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private boolean terminado;

	//bi-directional many-to-one association to Apuesta
	@OneToMany(mappedBy="eventoBean")
	@JsonIgnore
	private List<Apuesta> apuestas;

	//bi-directional many-to-one association to Categoria
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="categoria")
	@JsonIgnore
	private Categoria categoriaBean;

	//bi-directional many-to-one association to Participante
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="participante1")
	@JsonIgnore
	private Participante participante1Bean;

	//bi-directional many-to-one association to Participante
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="participante2")
	@JsonIgnore
	private Participante participante2Bean;

	//bi-directional many-to-one association to Resultado
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="resultado")
	@JsonIgnore
	private Resultado resultadoBean;

	public Evento() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getCuota1() {
		return this.cuota1;
	}

	public void setCuota1(float cuota1) {
		this.cuota1 = cuota1;
	}

	public float getCuota2() {
		return this.cuota2;
	}

	public void setCuota2(float cuota2) {
		this.cuota2 = cuota2;
	}

	public float getCuotaX() {
		return this.cuotaX;
	}

	public void setCuotaX(float cuotaX) {
		this.cuotaX = cuotaX;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean getTerminado() {
		return this.terminado;
	}

	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
	}

	public List<Apuesta> getApuestas() {
		return this.apuestas;
	}

	public void setApuestas(List<Apuesta> apuestas) {
		this.apuestas = apuestas;
	}

	public Apuesta addApuesta(Apuesta apuesta) {
		getApuestas().add(apuesta);
		apuesta.setEventoBean(this);

		return apuesta;
	}

	public Apuesta removeApuesta(Apuesta apuesta) {
		getApuestas().remove(apuesta);
		apuesta.setEventoBean(null);

		return apuesta;
	}

	public Categoria getCategoriaBean() {
		return this.categoriaBean;
	}

	public void setCategoriaBean(Categoria categoriaBean) {
		this.categoriaBean = categoriaBean;
	}

	public Participante getParticipante1Bean() {
		return this.participante1Bean;
	}

	public void setParticipante1Bean(Participante participante1Bean) {
		this.participante1Bean = participante1Bean;
	}

	public Participante getParticipante2Bean() {
		return this.participante2Bean;
	}

	public void setParticipante2Bean(Participante participante2Bean) {
		this.participante2Bean = participante2Bean;
	}

	public Resultado getResultadoBean() {
		return this.resultadoBean;
	}

	public void setResultadoBean(Resultado resultadoBean) {
		this.resultadoBean = resultadoBean;
	}

}