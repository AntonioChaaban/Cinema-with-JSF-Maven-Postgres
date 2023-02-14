package br.unitins.projeto1.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Ticket extends Ingresso{
	
	@Column(length = 100)
	private String codigoTicket;

	public String getCodigoTicket() {
		return codigoTicket;
	}

	public void setCodigoTicket(String codigoTicket) {
		this.codigoTicket = codigoTicket;
	}
	
	
}
