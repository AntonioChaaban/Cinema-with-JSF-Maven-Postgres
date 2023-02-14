package br.unitins.projeto1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Sala extends DefaultEntity<Sala>{

	@Column(length = 100)
	private String nomeDaSala;
	
	@Column(length = 2000)
	private Integer cadeiras;
	
	@ManyToOne
	private TipoSala tipoSala;
	
	@Column(length = 3)
	private Habilitador habilitador;

	
	public Habilitador getHabilitador() {
		return habilitador;
	}

	public void setHabilitador(Habilitador habilitador) {
		this.habilitador = habilitador;
	}

	public String getNomeDaSala() {
		return nomeDaSala;
	}

	public void setNomeDaSala(String nomeDaSala) {
		this.nomeDaSala = nomeDaSala;
	}

	public Integer getCadeiras() {
		return cadeiras;
	}

	public void setCadeiras(Integer cadeiras) {
		this.cadeiras = cadeiras;
	}

	public TipoSala getTipoSala() {
		return tipoSala;
	}

	public void setTipoSala(TipoSala tipoSala) {
		this.tipoSala = tipoSala;
	}

	
}
