package br.unitins.projeto1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ValorVenda extends DefaultEntity<ValorVenda>{

	@Column(length = 100)
	private String valorDaInteira;
	
	@Column(length = 100)
	private String valorDaMedia;
	
	@ManyToOne
	private Filmes filme;
	
	@Column
	private TiposDeSessao tipoDeSessao;
	
	@Column(length = 3)
	private Habilitador habilitador;

	public Habilitador getHabilitador() {
		return habilitador;
	}

	public void setHabilitador(Habilitador habilitador) {
		this.habilitador = habilitador;
	}

	public String getValorDaInteira() {
		return valorDaInteira;
	}

	public void setValorDaInteira(String valorDaInteira) {
		this.valorDaInteira = valorDaInteira;
	}

	public String getValorDaMedia() {
		return valorDaMedia;
	}

	public void setValorDaMedia(String valorDaMedia) {
		this.valorDaMedia = valorDaMedia;
	}

	public Filmes getFilme() {
		return filme;
	}

	public void setFilme(Filmes filme) {
		this.filme = filme;
	}

	public TiposDeSessao getTipoDeSessao() {
		return tipoDeSessao;
	}

	public void setTipoDeSessao(TiposDeSessao tipoDeSessao) {
		this.tipoDeSessao = tipoDeSessao;
	}
	
	
	
}
