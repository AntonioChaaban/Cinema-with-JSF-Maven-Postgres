package br.unitins.projeto1.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TipoSala extends DefaultEntity<TipoSala> {
	
	@Column(length = 100)
	private String nomeDoTipo;
	
	@Column(length = 10)
	private String letraDaFileira;
	
	@Column(length = 100)
	private String numeroDePoltronas;
	
	@Column(length = 3)
	private Habilitador habilitador;

	
	public Habilitador getHabilitador() {
		return habilitador;
	}

	public void setHabilitador(Habilitador habilitador) {
		this.habilitador = habilitador;
	}

	public String getNomeDoTipo() {
		return nomeDoTipo;
	}

	public void setNomeDoTipo(String nomeDoTipo) {
		this.nomeDoTipo = nomeDoTipo;
	}

	public String getLetraDaFileira() {
		return letraDaFileira;
	}

	public void setLetraDaFileira(String letraDaFileira) {
		this.letraDaFileira = letraDaFileira;
	}

	public String getNumeroDePoltronas() {
		return numeroDePoltronas;
	}

	public void setNumeroDePoltronas(String numeroDePoltronas) {
		this.numeroDePoltronas = numeroDePoltronas;
	}
	
	
}
