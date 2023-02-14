package br.unitins.projeto1.model;

public class AjudaSalaView {
	
	private String letraFileira;
	
	private String numeroDaPoltrona;
	
	private String icon;
	
	private boolean ocupado;
	

	public boolean isOcupado() {
		return ocupado;
	}

	public void setOcupado(boolean ocupado) {
		this.ocupado = ocupado;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLetraFileira() {
		return letraFileira;
	}

	public void setLetraFileira(String letraFileira) {
		this.letraFileira = letraFileira;
	}

	public String getNumeroDaPoltrona() {
		return numeroDaPoltrona;
	}

	public void setNumeroDaPoltrona(String numeroDaPoltrona) {
		this.numeroDaPoltrona = numeroDaPoltrona;
	}
	
	

}
