package br.unitins.projeto1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Ingresso extends DefaultEntity<Ingresso>{
	
	@Column(length = 100)
	private String valor;
	//ManyToOne
	@OneToOne
	private Sessao sessao;
	
	@Column(length = 100)
	private String codFiscal;
	
	@Column(length = 10)
	private String letraDaFileiraIngresso;
	
	@Column(length = 100)
	private String numeroDePoltronasIngresso;
	
	@ManyToOne
	private Usuario usuario;
	
	

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}

	public String getCodFiscal() {
		return codFiscal;
	}

	public void setCodFiscal(String codFiscal) {
		this.codFiscal = codFiscal;
	}

	public String getLetraDaFileiraIngresso() {
		return letraDaFileiraIngresso;
	}

	public void setLetraDaFileiraIngresso(String letraDaFileiraIngresso) {
		this.letraDaFileiraIngresso = letraDaFileiraIngresso;
	}

	public String getNumeroDePoltronasIngresso() {
		return numeroDePoltronasIngresso;
	}

	public void setNumeroDePoltronasIngresso(String numeroDePoltronasIngresso) {
		this.numeroDePoltronasIngresso = numeroDePoltronasIngresso;
	}
	
	
}
