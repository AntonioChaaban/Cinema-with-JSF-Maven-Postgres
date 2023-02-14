package br.unitins.projeto1.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.CreditCardNumber;

@Entity
public class Cartao extends DefaultEntity<Cartao>{
	
	@Column(length = 100)
	@CreditCardNumber(message = "numero de cartao invalido")
	private String numeroNoCartao;
	
	@Column(length = 4)
	private String cvc;
	
	@Column(length = 100)
	private String dataDeValidade;
	
	@Column(length = 100)
	private String nomeNoCartao;

	public String getNumeroNoCartao() {
		return numeroNoCartao;
	}

	public void setNumeroNoCartao(String numeroNoCartao) {
		this.numeroNoCartao = numeroNoCartao;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	public String getDataDeValidade() {
		return dataDeValidade;
	}

	public void setDataDeValidade(String dataDeValidade) {
		this.dataDeValidade = dataDeValidade;
	}

	public String getNomeNoCartao() {
		return nomeNoCartao;
	}

	public void setNomeNoCartao(String nomeNoCartao) {
		this.nomeNoCartao = nomeNoCartao;
	}
}
