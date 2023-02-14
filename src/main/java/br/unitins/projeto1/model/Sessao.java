package br.unitins.projeto1.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Sessao extends DefaultEntity<Sessao>{
	
	@Column
	private TiposDeSessao tipoDeSessao;
	
	@ManyToOne
	private Filmes filme;
	
	@ManyToOne
	private Sala sala;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicioSessao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataTerminoSessao;
	
	@OneToOne
	private ValorVenda valorVenda;

	@Column(length = 3)
	private Habilitador habilitador;

	public Habilitador getHabilitador() {
		return habilitador;
	}

	public void setHabilitador(Habilitador habilitador) {
		this.habilitador = habilitador;
	}
	
	public Sala getSala() {
		return sala;
	}

	public ValorVenda getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(ValorVenda valorVenda) {
		this.valorVenda = valorVenda;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	

	public TiposDeSessao getTipoDeSessao() {
		return tipoDeSessao;
	}

	public void setTipoDeSessao(TiposDeSessao tipoDeSessao) {
		this.tipoDeSessao = tipoDeSessao;
	}

	public Filmes getFilme() {
		return filme;
	}

	public void setFilme(Filmes filme) {
		this.filme = filme;
	}

	public Date getDataInicioSessao() {
		return dataInicioSessao;
	}

	public void setDataInicioSessao(Date dataInicioSessao) {
		this.dataInicioSessao = dataInicioSessao;
	}

	public Date getDataTerminoSessao() {
		return dataTerminoSessao;
	}

	public void setDataTerminoSessao(Date dataTerminoSessao) {
		this.dataTerminoSessao = dataTerminoSessao;
	}

	
}
