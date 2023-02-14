package br.unitins.projeto1.model;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Filmes extends DefaultEntity<Filmes>{

	@Column(length = 100)
	private String nome;
	
	@Column(length = 2000)
	private String sinopse;
	
	@Column(length = 600)
	private String elenco;
	
	@Column(length = 100)
	private String diretor;
	
	@Column(length = 100)
	private String genero;
	
	@Column(length = 60)
	private String durasao;
	
	@Column(length = 100)
	private String distribuidora;
	
	@Column(length = 100)
	private String classificasao;
	
	@Column(length = 100)
	private String imagemPoster;
	
	@Column(length = 100)
	private String imagemBackground;
	
	@Column(length = 3)
	private EstadoFilme estadoFilme;

	public String getImagemPoster() {
		return imagemPoster;
	}

	public void setImagemPoster(String imagemPoster) {
		this.imagemPoster = imagemPoster;
	}

	public String getImagemBackground() {
		return imagemBackground;
	}

	public void setImagemBackground(String imagemBackground) {
		this.imagemBackground = imagemBackground;
	}

	public EstadoFilme getEstadoFilme() {
		return estadoFilme;
	}

	public void setEstadoFilme(EstadoFilme estadoFilme) {
		this.estadoFilme = estadoFilme;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}

	public String getElenco() {
		return elenco;
	}

	public void setElenco(String elenco) {
		this.elenco = elenco;
	}

	public String getDiretor() {
		return diretor;
	}

	public void setDiretor(String diretor) {
		this.diretor = diretor;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getDurasao() {
		return durasao;
	}

	public void setDurasao(String durasao) {
		this.durasao = durasao;
	}

	public String getDistribuidora() {
		return distribuidora;
	}

	public void setDistribuidora(String distribuidora) {
		this.distribuidora = distribuidora;
	}

	public String getClassificasao() {
		return classificasao;
	}

	public void setClassificasao(String classificasao) {
		this.classificasao = classificasao;
	}
	
	
	
}
